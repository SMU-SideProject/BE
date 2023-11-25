package com.seoulog.seoulog.oauth;

import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;

public interface OauthApiClient {
    TokenDto getOauthAccessToken(OauthLoginRequest oauthLoginRequest);

    OauthProfileResponse getOauthProfile(String accessToken);

    User.Type getUserType();
}
