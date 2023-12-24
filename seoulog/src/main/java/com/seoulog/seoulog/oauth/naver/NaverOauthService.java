package com.seoulog.seoulog.oauth.naver;

import com.seoulog.seoulog.oauth.OauthApiClient;
import com.seoulog.seoulog.oauth.OauthInfo;
import com.seoulog.seoulog.oauth.OauthProfileResponse;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.repository.UserRepository;
import com.seoulog.seoulog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverOauthService {
    private final OauthApiClient naverApiClient;
    private final UserService userService;
    private final UserRepository userRepository;


    public OauthInfo getNaverInfo(NaverLoginRequest naverLoginRequest) {
        TokenDto naverToken = naverApiClient.getOauthAccessToken(naverLoginRequest);
        OauthProfileResponse oauthProfile = naverApiClient.getOauthProfile(naverToken.getAccessToken());
//        UserDto naverUserDto = principalDetails.getUserDto();

        //        userService.signup(naverUserDto);
//        log.info("save User={}", naverUserDto);

        return OauthInfo.builder()
                .refreshToken(naverToken.getRefreshToken())
                .email(oauthProfile.getEmail())
                .type(naverLoginRequest.userType())
                .nickname(oauthProfile.getNickName())
                .build();
    }

}
