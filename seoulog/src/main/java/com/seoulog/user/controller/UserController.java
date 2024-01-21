package com.seoulog.user.controller;

import com.seoulog.common.annotation.CurrentUser;
import com.seoulog.user.dto.AccessTokenDto;
import com.seoulog.user.dto.LoginDto;
import com.seoulog.user.oauth.OauthInfo;
import com.seoulog.user.oauth.OauthService;
import com.seoulog.user.oauth.kakao.KakaoLoginRequest;
import com.seoulog.user.oauth.kakao.KakaoOauthService;
import com.seoulog.user.oauth.naver.NaverLoginRequest;
import com.seoulog.user.oauth.naver.NaverOauthService;
import com.seoulog.user.dto.TokenDto;
import com.seoulog.user.dto.UserDto;
import com.seoulog.user.entity.User;
import com.seoulog.user.jwt.TokenProvider;
import com.seoulog.user.repository.UserRepository;
import com.seoulog.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final NaverLoginRequest naverLoginRequest;
    private final KakaoLoginRequest kakaoLoginRequest;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        userService.signup(userDto);
        return ResponseEntity.ok(null); //UserDto를 파라미터로 받아서 회원가입
    }

    @PostMapping("/signup/naver")
    public ResponseEntity<User> navreSignup(@RequestParam String code) {
        naverLoginRequest.setAuthorizationCode(code);
        OauthInfo naverInfo = naverOauthService.getNaverInfo(naverLoginRequest);
        return ResponseEntity.ok(oauthService.signup(naverInfo));
    }

    @PostMapping("/signup/kakao")
    public ResponseEntity<User> kakaoSignup(@RequestParam String code) {
        kakaoLoginRequest.setAuthorizationCode(code);
        OauthInfo kakaoInfo = kakaoOauthService.getKakaoInfo(kakaoLoginRequest);
        return ResponseEntity.ok(oauthService.signup(kakaoInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> postLogin(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        TokenDto tokenDto = userService.login(loginDto);
        userService.createCookie(tokenDto.getRefreshToken(), response);
        return new ResponseEntity<>(new AccessTokenDto(tokenDto.getAccessToken()), HttpStatus.OK);
    }

    @PostMapping("/login/naver")
    public ResponseEntity<AccessTokenDto> navreLogin(@RequestParam String code, HttpServletResponse response) {
        //refresh토큰 저장
        naverLoginRequest.setAuthorizationCode(code);
        OauthInfo naverInfo = naverOauthService.getNaverLoginInfo(naverLoginRequest);
        TokenDto tokenDto = oauthService.login(naverInfo, naverInfo.getType());
        userService.createCookie(tokenDto.getRefreshToken(), response);

        return new ResponseEntity<>(new AccessTokenDto(tokenDto.getAccessToken()), HttpStatus.OK);
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<AccessTokenDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        kakaoLoginRequest.setAuthorizationCode(code);
        //refresh토큰 저장
        OauthInfo kakaoInfo = kakaoOauthService.getKakaoLoginInfo(kakaoLoginRequest);
        TokenDto tokenDto = oauthService.login(kakaoInfo, kakaoInfo.getType());
        userService.createCookie(tokenDto.getRefreshToken(), response);

        return new ResponseEntity<>(new AccessTokenDto(tokenDto.getAccessToken()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CurrentUser User user, HttpServletRequest request, HttpServletResponse response) {
        //리프레시 토큰 삭제
        userService.logout(user, response);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
