package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.StoreReqDto;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public void createStore(@RequestBody StoreReqDto storeReqDto, UserInfo userInfo) {
        Store store = storeReqDto.toEntity(userInfo);

        storeRepository.save(store);

    }
}
