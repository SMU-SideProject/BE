package com.seoulog.seoulog.oauth.kakao;

import com.seoulog.seoulog.oauth.OauthLoginRequest;
import com.seoulog.seoulog.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
@Component
public class KakaoLoginRequest implements OauthLoginRequest {

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Schema(example = "1Ee8i2_T1j4Y0D7BXL7EDx6iLzLwes6V7Ans1wEFf_icqrFsssJ4wV1dXOEbLYBqSJfPGAo9dZwAAAGGgy4p6g")
    private String authorizationCode;

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public KakaoLoginRequest(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        return body;
    }

    @Override
    public User.Type userType() {
        return null;
    }
}
