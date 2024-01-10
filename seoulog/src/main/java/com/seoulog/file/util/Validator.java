package com.seoulog.file.util;

import com.seoulog.file.dto.UploadFilesDto;

public class Validator {
    private final static int FIFTEEN = 15;
    public static boolean isNumberOfImagesUnderFifteen(UploadFilesDto uploadFilesDto) {
        return uploadFilesDto.getImages().size() <= FIFTEEN;
    }
}
