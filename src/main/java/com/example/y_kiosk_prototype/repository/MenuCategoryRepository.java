package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.MenuCategory;
import com.example.y_kiosk_prototype.entity.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Integer> {
    List<MenuCategory> findMenuCategoryByMenuGroup(MenuGroup menuGroup);

    Optional<MenuCategory> findMenuCategoryByMenuCategoryId(int menuCategoryId);
}
