package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId> {

    Optional<Order> findFirstByOrderId_StoreIdAndOrderId_OrderTimeBetweenOrderByOrderId_OrderTimeDesc(
            String storeId,
            LocalDateTime start,
            LocalDateTime end
    );
}
