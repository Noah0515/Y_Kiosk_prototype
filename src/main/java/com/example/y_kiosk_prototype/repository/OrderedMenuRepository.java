package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuId;
import com.example.y_kiosk_prototype.entity.OrderedMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedMenuRepository extends JpaRepository<OrderedMenu, OrderedMenuId> {
}
