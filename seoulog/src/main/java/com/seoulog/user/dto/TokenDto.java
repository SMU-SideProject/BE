package com.seoulog.user.dto;

import lombok.*;
import net.minidev.json.JSONObject;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

}