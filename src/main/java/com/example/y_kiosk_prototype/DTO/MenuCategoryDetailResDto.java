package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class MenuCategoryDetailResDto {
    private int menuCategoryId;
    private String menuCategoryName;
    private List<MenuDetailResDto> menuDetailResDtoList;

    public static MenuCategoryDetailResDto from(MenuCategory menuCategory) {
        MenuCategoryDetailResDto menuCategoryDetailResDto = MenuCategoryDetailResDto.builder()
                .menuCategoryId(menuCategory.getMenuCategoryId())
                .menuCategoryName(menuCategory.getMenuCategoryName())
                .menuDetailResDtoList(menuCategory.getMenus() == null ? Collections.emptyList() :
                        menuCategory
                                .getMenus()
                                .stream()
                                .map(MenuDetailResDto::from)
                                .toList())
                .build();

        return menuCategoryDetailResDto;
    }
}
