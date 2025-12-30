package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class MenuGroupDetailResDto {
    private String menuGroupName;
    private int menuGroupId;
    private List<MenuCategoryDetailResDto> menuCategoryDetailResDtoList;

    public static MenuGroupDetailResDto from(MenuGroup menuGroup) {
        MenuGroupDetailResDto menuGroupDetailResDto = MenuGroupDetailResDto.builder()
                .menuGroupName(menuGroup.getMenuGroupName())
                .menuGroupId(menuGroup.getMenuGroupId())
                .menuCategoryDetailResDtoList(menuGroup.getMenuCategories() == null ? Collections.emptyList() :
                        menuGroup
                        .getMenuCategories()
                        .stream()
                        .map(MenuCategoryDetailResDto::from)
                        .toList())
                .build();

        return menuGroupDetailResDto;
    }
}
