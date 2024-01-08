package com.seoulog.file.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class ImageNameDto {
    private final String fileName;

    public ImageNameDto(String fileName) {
        this.fileName = fileName;
    }
}
