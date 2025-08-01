package com.I2.yakpt.user.service;

import com.I2.yakpt.email.service.EmailVerificationStore;
import com.I2.yakpt.exception.CustomException;
import com.I2.yakpt.exception.ErrorCode;
import com.I2.yakpt.user.dto.*;
import com.I2.yakpt.user.entity.UserEntity;
import com.I2.yakpt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationStore verificationStore;

    public void registerUser(SignUpRequestDto request) {
        if (userRepository.existsById(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (!verificationStore.isAuthenticated(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.save(user);
    }

    public void resetPassword(PasswordResetRequestDto request) {
        if (!verificationStore.isValid(request.getEmail(), request.getCode())) {
            throw new IllegalArgumentException("인증번호가 올바르지 않거나 만료되었습니다.");
        }

        UserEntity user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}