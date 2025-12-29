package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.StoreState;
import com.example.y_kiosk_prototype.entity.MenuGroup;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import com.example.y_kiosk_prototype.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MenuGroupReqDto {
    private final StoreService storeService;
    private String groupName;
    private String storeId;

    public MenuGroup toEntity(Store store) {
        IdGenerator idGenerator = new IdGenerator();
        return MenuGroup.builder()
                .menuGroupId(idGenerator.generateIntId(Integer.MAX_VALUE))
                .menuGroupName(groupName)
                .store(store)
                .build();
    }
}
