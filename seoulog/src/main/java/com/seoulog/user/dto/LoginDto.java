package com.seoulog.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {


    @NotNull
    @Size(min = 3, max = 100)
    private String password;
    @NotNull
    private String email;
    private String accessToken;
    private String tokenType;
}