package com.seoulog.user.oauth;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import com.seoulog.user.dto.LoginDto;
import com.seoulog.common.tokenDto.TokenDto;
import com.seoulog.user.entity.User;
import com.seoulog.user.oauth.kakao.KakaoOauthService;
import com.seoulog.user.oauth.naver.NaverOauthService;
import com.seoulog.user.repository.UserRepository;
import com.seoulog.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OauthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final NaverOauthService naverOauthService;
    private final KakaoOauthService kakaoOauthService;
    private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Transactional
    public User signup(OauthInfo userDto) { //회원가입 메소드

        if (userRepository.findByOauthId(userDto.getId()) != null) {
            throw new BusinessException(ErrorCode.SIGNUP_USER_EXIST);
        } else if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()) != null) {
            throw new BusinessException(ErrorCode.SIGNUP_EMAIL_EXIST);
        }

        User user = User.builder()
                .password(encoder.encode(userDto.getId())) //비번 임의로 지정
                .nickname(userDto.getNickname()+userDto.getId()) //일반 회원가입 한 회원이랑 닉네임이 겹치는 거 방지하기 위해 고유id추가
                .email(userDto.getEmail())
                .activated(true)
                .type(userDto.getType())
                .refreshToken(userDto.getRefreshToken())
                .oauthId(userDto.getId())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(OauthInfo oauthInfo, User.Type type) {
        LoginDto loginDto = LoginDto.builder()
                .password(oauthInfo.getId())
                .email(oauthInfo.getEmail())
                .build();
        return userService.login(loginDto);

    }

}


