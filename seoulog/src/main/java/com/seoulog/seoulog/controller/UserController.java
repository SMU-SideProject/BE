package com.seoulog.seoulog.controller;

import com.seoulog.seoulog.config.oauth.OauthInfo;
import com.seoulog.seoulog.config.oauth.OauthService;
import com.seoulog.seoulog.config.oauth.kakao.KakaoLoginRequest;
import com.seoulog.seoulog.config.oauth.kakao.KakaoOauthService;
import com.seoulog.seoulog.config.oauth.naver.NaverLoginRequest;
import com.seoulog.seoulog.config.oauth.naver.NaverOauthService;
import com.seoulog.seoulog.dto.LoginDto;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.dto.UserDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.jwt.TokenProvider;
import com.seoulog.seoulog.repository.UserRepository;
import com.seoulog.seoulog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final NaverOauthService naverOauthService;
    private final KakaoOauthService kakaoOauthService;
    private final OauthService oauthService;
    private final String regx = "^(.+)@(.+)$";
    private final Pattern pattern = Pattern.compile(regx);


    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        if(!pattern.matcher(userDto.getEmail()).matches()){
            return new ResponseEntity<>("이메일 형식을 정확히 입력해주세요.", HttpStatus.BAD_REQUEST);

        }
        else if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()) != null) {
            return new ResponseEntity<>("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        } else if(userRepository.findByNickname(userDto.getNickname()) != null){
            return new ResponseEntity<>("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }
        User user = userService.signup(userDto);
        return ResponseEntity.ok(null); //UserDto를 파라미터로 받아서 회원가입
    }

}
