package com.seoulog.seoulog.service;

import com.seoulog.seoulog.config.auth.PrincipalDetails;
import com.seoulog.seoulog.dto.LoginDto;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.dto.UserDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.jwt.TokenProvider;
import com.seoulog.seoulog.repository.RefreshTokenRepository;
import com.seoulog.seoulog.repository.UserRepository;
//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public User signup(UserDto userDto) { //회원가입 메소드
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
        System.out.println("loginDto.getPassword() = " + loginDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        System.out.println("authenticationToken = " + authenticationToken);

        System.out.println("loginDto.getEmail() = " + loginDto.getEmail());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //authentication token을 이용해서 authenticate메소드가 실행될때 loadUserByUsername이 실행
        System.out.println("authenticationManagerBuilder.getObject() = " + authenticationManagerBuilder.getObject());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("user 인증객체얻기 성공");

        //refreshToken은 DB에 저장
        refreshTokenRepository.saveToken(refreshToken, principalDetails.getUser().getEmail());

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        System.out.println("UserService.login");
        return tokenDto;

    }


}