package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class StoreOrderResDto {
    private String storeId;
    private String storeName;
    private List<OrderResDto> orders;

    public static StoreOrderResDto from(Store store) {
        StoreOrderResDto storeOrderResDto = StoreOrderResDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .orders(store.getOrders() == null ? Collections.emptyList() :
                        store.getOrders()
                                .stream()
                                .map(OrderResDto::from)
                                .toList())
                .build();

        return storeOrderResDto;
    }
}
