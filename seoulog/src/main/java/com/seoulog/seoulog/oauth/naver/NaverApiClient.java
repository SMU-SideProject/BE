package com.seoulog.seoulog.oauth.naver;

import com.seoulog.seoulog.oauth.OauthApiClient;
import com.seoulog.seoulog.oauth.OauthLoginRequest;
import com.seoulog.seoulog.oauth.OauthProfileResponse;
import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.Objects;
/*
  security:
          oauth2:
          client:
          registration:*/
@Component
@Slf4j
public class NaverApiClient implements OauthApiClient {

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String authUrl;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String apiUrl;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public NaverApiClient() {
        this.restTemplate = new RestTemplate();
    }


    @Override
    public TokenDto getOauthAccessToken(OauthLoginRequest oauthLoginRequest) {
//        String url = authUrl + "/oauth2.0/token";
        HttpHeaders httpHeaders = newHttpHeaders();

        MultiValueMap<String, String> body = oauthLoginRequest.makeBody();
        body.add("client_secret", clientSecret);

        System.out.println("body = " + body);
        System.out.println("body.get(state) = " + body.get("state"));


        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<NaverToken> response = restTemplate.postForEntity(authUrl, request, NaverToken.class); //body에 naverToken 반환

        return new TokenDto(Objects.requireNonNull(response.getBody()).getAccessToken(), response.getBody().getRefreshToken()); //Token 객체 반환
    }

    @Override
    public OauthProfileResponse getOauthProfile(String accessToken) {
//        String url = apiUrl + "/v1/nid/me";

        HttpHeaders httpHeaders = newHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
//        ResponseEntity<OauthInfo> response = restTemplate.postForEntity(apiUrl, request, OauthInfo.class);
        ResponseEntity<NaverMyInfo> response = restTemplate.postForEntity(apiUrl, request, NaverMyInfo.class);


        log.info("getOauthProfile.response={}", response);

        return response.getBody();
    }

    @Override
    public User.Type getUserType() {
        return User.Type.NAVER;
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
