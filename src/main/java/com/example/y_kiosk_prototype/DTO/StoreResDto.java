package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.StoreState;
import com.example.y_kiosk_prototype.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class StoreResDto {
    private String storeName;
    private String storeId;
    private StoreState state;

    public static StoreResDto from(Store store) {
        StoreResDto storeResDto = StoreResDto.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .state(store.getState())
                .build();

        return storeResDto;
    }
}
