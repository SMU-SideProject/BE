package com.seoulog.file.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class UploadFilesDto {
    private final List<String> images;

    public UploadFilesDto(List<String> images) {
        this.images = images;
    }
}
