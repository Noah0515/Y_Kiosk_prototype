package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuOptionId;
import com.example.y_kiosk_prototype.entity.OrderedMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderedMenuOptionRepository extends JpaRepository<OrderedMenuOption, OrderedMenuOptionId> {

    //Optional<OrderedMenuOption> findOrderedMenuOptionBy
}
