package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuCategory;
import com.example.y_kiosk_prototype.entity.MenuGroup;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuCategoryReqDto {
    private String menuCategoryName;
    private int menuGroupId;

    public MenuCategory toEntity(MenuGroup menuGroup) {
        return MenuCategory.builder()
                .menuCategoryId(IdGenerator.generateIntId(Integer.MAX_VALUE))
                .menuCategoryName(menuCategoryName)
                .menuGroup(menuGroup)
                .build();
    }
}
