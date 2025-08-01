package com.I2.yakpt.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailVerificationStore store;

    public void sendVerificationCode(String email) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        store.saveCode(email, code, 3);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("moox4@naver.com");
        message.setSubject("[방구석 약사] 인증번호");
        message.setText("인증번호: " + code);

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        return store.isValid(email, code);
    }
}
