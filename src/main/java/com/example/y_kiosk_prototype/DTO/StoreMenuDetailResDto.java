package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class StoreMenuDetailResDto {
    private String storeName;
    private String storeId;
    private List<MenuGroupDetailResDto> menuGroupDetailResDtoList;

    public static StoreMenuDetailResDto from(Store store) {
        StoreMenuDetailResDto storeMenuDetailResDto = StoreMenuDetailResDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .menuGroupDetailResDtoList(store.getMenuGroups() == null ? Collections.emptyList() :
                        store
                                .getMenuGroups()
                                .stream()
                                .map(MenuGroupDetailResDto::from)
                                .toList())
                .build();

        return storeMenuDetailResDto;
    }
}
