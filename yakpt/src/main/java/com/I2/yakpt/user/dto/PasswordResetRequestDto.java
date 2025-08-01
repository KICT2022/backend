package com.I2.yakpt.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    private String email;           // 사용자 이메일
    private String code;        // 사용자가 입력한 인증번호
    private String newPassword;     // 새 비밀번호
    private String confirmPassword; // 새 비밀번호 확인 (서로 일치하는지 서비스에서 검증)
}