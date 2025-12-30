package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.StoreMenuDetailResDto;
import com.example.y_kiosk_prototype.DTO.StoreReqDto;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.StoreRepository;
import com.example.y_kiosk_prototype.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;

    public Store createStore(@RequestBody StoreReqDto storeReqDto, UserInfo userInfo) {
        Store store = storeReqDto.toEntity(userInfo);

        storeRepository.save(store);
        log.info("Store {} created", store.getStoreId());

        return store;
    }

    public List<Store> findAllStoresByUserInfo(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        List<Store> stores = storeRepository.findAllByUserInfo_UserId(userId);

        log.info("stores number : {}", stores.size());
        return stores;
    }

    public Store findStoreById(String storeId) {
        Optional<Store> store = storeRepository.findById(storeId);

        return store.orElse(null);
    }

    @Transactional(readOnly = true)
    public StoreMenuDetailResDto getFullStoreMenu(String storeId) {
        Store store = findStoreById(storeId);

        return StoreMenuDetailResDto.from(store);
    }

}
