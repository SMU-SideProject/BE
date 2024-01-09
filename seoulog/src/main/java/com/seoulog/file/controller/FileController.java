package com.seoulog.file.controller;

import com.seoulog.file.dto.PreSignedUrlsDto;
import com.seoulog.file.dto.UploadFilesDto;
import com.seoulog.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/presigned")
    public ResponseEntity<PreSignedUrlsDto> createPreSigned(@RequestBody UploadFilesDto uploadFilesDto) {
        PreSignedUrlsDto preSignedUrlsDto = fileService.getPreSignedUrls(uploadFilesDto);
        return ResponseEntity.ok(preSignedUrlsDto);
    }

}
