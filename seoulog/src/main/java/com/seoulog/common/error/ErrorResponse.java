package com.seoulog.common.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;

    ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.getMessage());
    }
}
