package com.seoulog.file.service;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import com.seoulog.file.dto.PreSignedUrlsDto;
import com.seoulog.file.dto.UploadFilesDto;
import com.seoulog.file.util.Validator;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final String path = "img";

    private final PreSignedUrlService preSignedUrlService;

    public PreSignedUrlsDto getPreSignedUrls(UploadFilesDto uploadFilesDto) {
        Map<String, String> preSignedUrls = new HashMap<>();

        if(Validator.isNumberOfImagesUnderFifteen(uploadFilesDto)) {
            uploadFilesDto.getImages()
                    .forEach(i -> {
                        String fileId = preSignedUrlService.createFileId();

                        preSignedUrls.put(
                                fileId,
                                preSignedUrlService.makePreSignedUrl(path, fileId + i)
                        );
                    });

            return new PreSignedUrlsDto(preSignedUrls);
        }
        throw new BusinessException(ErrorCode.TOO_MANY_IMAGES);
    }
}
