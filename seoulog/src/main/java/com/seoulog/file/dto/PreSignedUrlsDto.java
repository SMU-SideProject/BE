package com.seoulog.file.dto;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class PreSignedUrlsDto {
    private final Map<String, String> preSignedUrls;

    public PreSignedUrlsDto(Map<String, String> preSignedUrls) {
        this.preSignedUrls = preSignedUrls;
    }
}