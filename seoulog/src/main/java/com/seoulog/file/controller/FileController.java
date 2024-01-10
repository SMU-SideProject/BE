package com.seoulog.file.controller;

import com.seoulog.common.annotation.CurrentUser;
import com.seoulog.file.dto.PreSignedUrlsDto;
import com.seoulog.file.dto.UploadFilesDto;
import com.seoulog.file.service.FileService;
import com.seoulog.user.entity.User;
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
    public ResponseEntity<PreSignedUrlsDto> createPreSigned(
            /* 추후 토큰 추가 시 유저에 대한 정보를 받아와 db에 저장합니다.
            @CurrentUser User user,
             */
            @RequestBody UploadFilesDto uploadFilesDto
    ) {
        PreSignedUrlsDto preSignedUrlsDto = fileService.getPreSignedUrls(uploadFilesDto);
        return ResponseEntity.ok(preSignedUrlsDto);
    }

}
