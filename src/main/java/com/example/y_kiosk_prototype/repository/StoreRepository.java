package com.example.y_kiosk_prototype.repository;

import com.example.y_kiosk_prototype.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    Optional<Store> findByStoreId(String storeId);

    List<Store> findAllByUserInfo_UserId(String userId);
}
