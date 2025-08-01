package com.I2.yakpt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "E001", "이미 등록된 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "E002", "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "E003", "이메일 인증이 완료되지 않았습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E004", "사용자를 찾을 수 없습니다."),
    CODE_INVALID_OR_EXPIRED(HttpStatus.BAD_REQUEST, "E005", "인증번호가 올바르지 않거나 만료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
