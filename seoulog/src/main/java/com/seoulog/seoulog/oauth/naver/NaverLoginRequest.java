package com.seoulog.seoulog.oauth.naver;

import com.seoulog.seoulog.oauth.OauthLoginRequest;
import com.seoulog.seoulog.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.security.SecureRandom;

//토큰을 요청하는 URL의 형식은
//https://nid.naver.com/oauth2.0/token?client_id={클라이언트 아이디}&client_secret={클라이언트 시크릿}&grant_type=authorization_code&state={상태 토큰}&code={인증 코드}
//이므로, DTO에서 받은 변수들을 조합합니다
@Getter
@NoArgsConstructor
@Component
public class NaverLoginRequest implements OauthLoginRequest {
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Schema(example = "jYHm2t6tggTCSsb50x")
    private String authorizationCode;

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        SecureRandom random = new SecureRandom();

        body.add("grant_type",grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);
        body.add("state", new BigInteger(130, random).toString());
        System.out.println(body); //여기까지 실행

        return body;
    }

    @Override
    public User.Type userType() {
        return User.Type.NAVER;
    }
}
