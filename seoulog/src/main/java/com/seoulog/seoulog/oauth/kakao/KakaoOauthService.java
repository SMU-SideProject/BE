package com.seoulog.seoulog.oauth.kakao;

import com.seoulog.seoulog.oauth.OauthApiClient;
import com.seoulog.seoulog.oauth.OauthInfo;
import com.seoulog.seoulog.oauth.OauthLoginRequest;
import com.seoulog.seoulog.oauth.OauthProfileResponse;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthService {
    private final OauthApiClient kakaoApiClient;
    private final UserRepository userRepository;
    public OauthInfo getKakaoInfo(OauthLoginRequest oauthLoginRequest) {
        TokenDto kakaoToken = kakaoApiClient.getOauthAccessToken(oauthLoginRequest);
        OauthProfileResponse oauthProfile = kakaoApiClient.getOauthProfile(kakaoToken.getAccessToken());

//        UserDto kakaoUser = principalDetails.getUserDto();
        //아직 회원가입을 하지 않았다면 회원가입 진행
        //        userRepository.save(user);
//        log.info("save User={}", kakaoUser);

        return OauthInfo.builder()
                .refreshToken(kakaoToken.getRefreshToken())
                .email(oauthProfile.getEmail())
                .type(User.Type.NAVER)
                .nickname(oauthProfile.getNickName())
                .build();
    }
}
