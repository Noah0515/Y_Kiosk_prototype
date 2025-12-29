package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.MenuGroup;
import com.example.y_kiosk_prototype.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Integer> {
    List<MenuGroup> findAllByStore(Store store);
}
