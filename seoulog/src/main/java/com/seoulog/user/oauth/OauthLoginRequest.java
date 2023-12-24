package com.seoulog.user.oauth;

import com.seoulog.user.entity.User;
import org.springframework.util.MultiValueMap;

public interface OauthLoginRequest {
    MultiValueMap<String, String> makeBody();

    User.Type userType();
}
