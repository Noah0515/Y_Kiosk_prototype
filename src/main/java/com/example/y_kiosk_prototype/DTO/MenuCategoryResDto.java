package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MenuCategoryResDto {
    private int menuCategoryId;
    private int menuGroupId;
    private String menuCategoryName;

    public static MenuCategoryResDto from(MenuCategory menuCategory) {
        return MenuCategoryResDto.builder()
                .menuCategoryId(menuCategory.getMenuCategoryId())
                .menuGroupId(menuCategory.getMenuCategoryId())
                .menuCategoryName(menuCategory.getMenuCategoryName())
                .build();
    }
}
