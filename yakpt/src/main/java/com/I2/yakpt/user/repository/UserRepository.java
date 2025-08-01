package com.I2.yakpt.user.repository;

import com.I2.yakpt.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmail(String email);         // 이메일 중복 확인
    Optional<UserEntity> findByEmail(String email); // 로그인 시 이메일로 사용자 조회

}
