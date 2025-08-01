package com.I2.yakpt.user.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String gender;
    private String birthDate;
    private String phoneNumber;
}
