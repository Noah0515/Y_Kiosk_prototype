package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, Integer> {
}
