package com.seoulog.file.controller;

import com.seoulog.file.dto.ImageNameDto;
import com.seoulog.file.service.PreSignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final PreSignedUrlService preSignedUrlService;
    private String path;

    @PostMapping("/presigned")
    public String createPreSigned(@RequestBody ImageNameDto imageNameDto) {
        path ="test";
        String imageName = imageNameDto.getFileName();
        return preSignedUrlService.getPreSignedUrl(path, imageName);
    }

}
