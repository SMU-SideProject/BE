package com.seoulog.file.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class PreSignedUrlsDto {
    private final List<String> preSignedUrls;

    public PreSignedUrlsDto(List<String> preSignedUrls) {
        this.preSignedUrls = preSignedUrls;
    }
}