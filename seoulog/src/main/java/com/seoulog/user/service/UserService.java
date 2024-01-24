package com.seoulog.user.service;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import com.seoulog.user.dto.LoginDto;
import com.seoulog.common.tokenDto.TokenDto;
import com.seoulog.user.dto.UserDto;
import com.seoulog.user.entity.User;
import com.seoulog.common.jwt.TokenProvider;
import com.seoulog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final String regx = "^(.+)@(.+)$";
    private final Pattern pattern = Pattern.compile(regx);
    private final String COOKIE_NAME = "refresh-token";
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Transactional
    public User signup(UserDto userDto) { //회원가입 메소드

        if(!pattern.matcher(userDto.getEmail()).matches()){
            throw new BusinessException(ErrorCode.SIGNUP_EMAIL_ERROR_TYPE);
        }
        else if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()) != null) {
            throw new BusinessException(ErrorCode.SIGNUP_EMAIL_EXIST);

        } else if(userRepository.findByNickname(userDto.getNickname()) != null){
            throw new BusinessException(ErrorCode.SIGNUP_REDUNDANT_NICKNAME);
        }

        User user = User.builder()
                .password(encoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .activated(true)
                .type(User.Type.NATIVE)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(@RequestBody LoginDto loginDto) {

        User user = userRepository.findOneWithAuthoritiesByEmail(loginDto.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        if (user == null) {
            throw new BusinessException(ErrorCode.SIGNUP_EMAIL_NOT_EXIST);
        } else if (!encoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.SIGNUP_PASSWORD_WRONG);
        }
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //authentication token을 이용해서 authenticate메소드가 실행될때 loadUserByUsername이 실행
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        //refreshToken은 DB에 저장
        user.updateRefreshToken(refreshToken);

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        return tokenDto;

    }
    public void createCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_NAME, refreshToken)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge((60 * 1000) * 60 * 24 * 7)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void deleteCookie(HttpServletResponse response) {

        ResponseCookie deleteCookie = ResponseCookie.from(COOKIE_NAME, "")
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }


    @Transactional
    public void logout(User user, HttpServletResponse response) {
        deleteCookie(response);
        User logoutUser = userRepository.findOneWithAuthoritiesByEmail(user.getEmail());
        logoutUser.updateRefreshToken(null);
    }
}