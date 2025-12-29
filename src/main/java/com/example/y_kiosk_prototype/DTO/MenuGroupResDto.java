package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class MenuGroupResDto {
    private int menuGroupId;
    private String menuGroupName;
    private String storeId;

    public static MenuGroupResDto from(MenuGroup menuGroup) {
        return MenuGroupResDto.builder()
                .menuGroupId(menuGroup.getMenuGroupId())
                .menuGroupName(menuGroup.getMenuGroupName())
                .storeId(menuGroup.getStore().getStoreId())
                .build();
    }

}
