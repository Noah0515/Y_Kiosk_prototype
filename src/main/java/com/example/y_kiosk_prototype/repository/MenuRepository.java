package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findMenuByMenuId(int menuId);

}
