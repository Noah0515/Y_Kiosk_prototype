package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.StoreReqDto;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
}
