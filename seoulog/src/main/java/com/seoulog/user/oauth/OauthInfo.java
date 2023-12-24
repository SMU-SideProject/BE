package com.seoulog.user.oauth;

import com.seoulog.user.entity.User;
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
