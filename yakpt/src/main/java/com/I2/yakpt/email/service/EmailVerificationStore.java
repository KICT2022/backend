package com.I2.yakpt.email.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailVerificationStore {
    private record CodeEntry(String code, LocalDateTime expiresAt) {}

    private final ConcurrentHashMap<String, CodeEntry> storage = new ConcurrentHashMap<>();

    public void saveCode(String email, String code, int minutesValid) {
        storage.put(email, new CodeEntry(code, LocalDateTime.now().plusMinutes(minutesValid)));
    }


    public boolean isValid(String email, String code) {
        CodeEntry entry = storage.get(email);
        return entry != null && entry.code().equals(code) && LocalDateTime.now().isBefore(entry.expiresAt());
    }

    public boolean isAuthenticated(String email) {
        CodeEntry entry = storage.get(email);
        // entry가 있고, 현재 시간이 만료시간 이전이면 인증 완료로 간주
        return entry != null && LocalDateTime.now().isBefore(entry.expiresAt());
    }

}
