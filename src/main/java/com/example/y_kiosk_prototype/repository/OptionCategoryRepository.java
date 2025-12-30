package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.composite_key.OptionCategoryId;
import com.example.y_kiosk_prototype.entity.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, OptionCategoryId> {
}
