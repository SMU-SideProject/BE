package com.seoulog.seoulog.oauth;

import com.seoulog.seoulog.entity.User;
import org.springframework.util.MultiValueMap;

public interface OauthLoginRequest {
    MultiValueMap<String, String> makeBody();

    User.Type userType();
}
