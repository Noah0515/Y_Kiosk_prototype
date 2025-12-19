package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, String> {
    Optional<SocialLogin> findByKakaoEmail(String email);

    Optional<SocialLogin> findByUserId(String userId);

    Optional<SocialLogin> findByKakaoId(String kakaoId);
}
