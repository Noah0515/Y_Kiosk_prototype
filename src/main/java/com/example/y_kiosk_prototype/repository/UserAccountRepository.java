package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
