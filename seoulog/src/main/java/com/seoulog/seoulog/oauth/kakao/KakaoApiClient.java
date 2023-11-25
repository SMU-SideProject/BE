package com.seoulog.seoulog.oauth.kakao;

import com.seoulog.seoulog.oauth.OauthApiClient;
import com.seoulog.seoulog.oauth.OauthLoginRequest;
import com.seoulog.seoulog.oauth.OauthProfileResponse;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class KakaoApiClient implements OauthApiClient {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String authUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    public KakaoApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    @Override
    public TokenDto getOauthAccessToken(OauthLoginRequest oauthLoginRequest) {
//        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = newHttpHeaders();

        MultiValueMap<String, String> body = oauthLoginRequest.makeBody();
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KakaoToken> response = restTemplate.postForEntity(authUrl, request, KakaoToken.class);

        return new TokenDto(Objects.requireNonNull(response.getBody()).getAccessToken(), response.getBody().getRefreshToken());
    }

    @Override
    public OauthProfileResponse getOauthProfile(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = newHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KakaoMyInfo> response = restTemplate.postForEntity(apiUrl, request, KakaoMyInfo.class);
//        ResponseEntity<OauthInfo> response = restTemplate.postForEntity(apiUrl, request, OauthInfo.class);
//        ResponseEntity<NaverMyInfo> response = restTemplate.postForEntity(apiUrl, request, NaverMyInfo.class);

        return response.getBody();
    }

    @Override
    public User.Type getUserType() {
        return null;
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
