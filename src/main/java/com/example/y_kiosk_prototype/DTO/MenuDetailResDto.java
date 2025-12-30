package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.MenuState;
import com.example.y_kiosk_prototype.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class MenuDetailResDto {
    private int menuId;
    private String menuName;
    private String menuInfo;
    private String allergy;
    private MenuState menuState;
    private String imageUrl;
    private List<MenuOptionDetailResDto> menuOptionDetailResDtoList;

    public static MenuDetailResDto from(Menu menu) {
        MenuDetailResDto menuDetailResDto = MenuDetailResDto.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getMenuName())
                .menuInfo(menu.getMenuInfo())
                .allergy(menu.getAllergy())
                .menuState(menu.getMenuState())
                .imageUrl(menu.getImageUrl())
                .menuOptionDetailResDtoList(menu.getMenuOptions() == null ? Collections.emptyList() :
                        menu.getMenuOptions()
                                .stream()
                                .map(MenuOptionDetailResDto::from)
                                .toList())
                .build();

        return menuDetailResDto;
    }
}
