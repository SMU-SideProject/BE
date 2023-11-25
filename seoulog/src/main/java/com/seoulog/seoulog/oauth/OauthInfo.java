package com.seoulog.seoulog.oauth;

import com.seoulog.seoulog.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthInfo {
    private final String nickname;
    private final String email;
    private final User.Type type;
    private final String refreshToken;
}
