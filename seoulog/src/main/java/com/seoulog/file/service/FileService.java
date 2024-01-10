package com.seoulog.file.service;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import com.seoulog.file.dto.PreSignedUrlsDto;
import com.seoulog.file.dto.UploadFilesDto;
import com.seoulog.file.util.Validator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final String path = "img";

    private final PreSignedUrlService preSignedUrlService;

    public PreSignedUrlsDto getPreSignedUrls(UploadFilesDto uploadFilesDto) {
        if(Validator.isNumberOfImagesUnderFifteen(uploadFilesDto)) {
            List<String> preSignedUrls =
                    uploadFilesDto.getImages().stream()
                            .map(i -> preSignedUrlService.getPreSignedUrl(path, i))
                            .toList();

            return new PreSignedUrlsDto(preSignedUrls);
        }
        throw new BusinessException(ErrorCode.TOO_MANY_IMAGES);
    }
}
