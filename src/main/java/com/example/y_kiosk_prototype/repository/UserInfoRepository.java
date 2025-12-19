package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.composite_key.UsersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, UsersId> {
    Optional<UserInfo> findByUserId(String userId);
}
