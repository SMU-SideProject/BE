package com.seoulog.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생하였습니다."),

    //request error
    SIGNUP_EMAIL_EMPTY(HttpStatus.BAD_REQUEST, "이메일을 입력해 주세요."),
    SIGNUP_EMAIL_ERROR_TYPE(HttpStatus.BAD_REQUEST, "이메일 형식을 정확히 입력해주세요."),
    SIGNUP_EMAIL_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 회원이 존재하지 않습니다."),
    SIGNUP_PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호를 입력해 주세요"),
    SIGNUP_NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "닉네임을 입력해 주세요"),
    SIGNUP_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호의 길이는 3자리 이상 입력해주세요."),

    //response error
    SIGNUP_REDUNDANT_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다."),
    SIGNUP_PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "비밀번호가 잘못 되었습니다."),
    SIGNUP_EMAIL_EXIST(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    SIGNUP_USER_EXIST(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),

    //file err
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "최대 이미지 수를 초과하였습니다."),

    //jwt error
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 엑세스 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 엑세스 토큰입니다."),
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 엑세스 토큰입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "JWT 토큰이 잘못되었습니다."),
    TOKEN_EMPTY(HttpStatus.BAD_REQUEST, "로그아웃한 회원입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
