package com.seoulog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BusinessException extends RuntimeException{
    ErrorCode errorCode;
}
