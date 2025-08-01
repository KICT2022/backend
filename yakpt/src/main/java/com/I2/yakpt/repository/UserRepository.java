// 경로: src/main/java/com/yourcompany/projectname/repository/user/UserRepository.java
package com.I2.yakpt.repository;

import com.I2.yakpt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
