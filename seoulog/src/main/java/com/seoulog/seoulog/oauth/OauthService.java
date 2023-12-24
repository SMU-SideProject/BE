package com.seoulog.seoulog.oauth;

import com.seoulog.seoulog.dto.LoginDto;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.repository.UserRepository;
import com.seoulog.seoulog.service.UserService;
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
    private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Transactional
    public User signup(OauthInfo userDto) { //회원가입 메소드
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        User user = User.builder()
                .password(encoder.encode(userDto.getNickname())) //비번 임의로 지정
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .activated(true)
                .type(userDto.getType())
                .refreshToken(userDto.getRefreshToken())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(OauthInfo oauthInfo) {
        LoginDto loginDto = LoginDto.builder()
                .password(oauthInfo.getNickname())
                .email(oauthInfo.getEmail())
                .build();
        return userService.login(loginDto);

    }

}


