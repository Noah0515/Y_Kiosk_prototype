package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.StoreState;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreReqDto {
    private String storeName;

    public Store toEntity(UserInfo userInfo) {
        IdGenerator idGenerator = new IdGenerator();
        return Store.builder()
                .storeId(idGenerator.generateStringId(60))
                .storeName(storeName)
                .verifyCode(null)
                .state(StoreState.CLOSED)
                .create_date(LocalDate.now())
                .userInfo(userInfo)
                .build();
    }
}
