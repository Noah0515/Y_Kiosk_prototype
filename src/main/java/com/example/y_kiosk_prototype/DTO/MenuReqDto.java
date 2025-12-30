package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.MenuState;
import com.example.y_kiosk_prototype.entity.Menu;
import com.example.y_kiosk_prototype.entity.MenuCategory;
import com.example.y_kiosk_prototype.entity.MenuOption;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuReqDto {
    private String menuName;
    private String menuInfo;
    private String allergy;
    private int menuCategoryId;
    private List<MenuOptionReqDto> menuOptions;

    public Menu toEntity(MenuCategory menuCategory) {
        Menu menu = Menu.builder()
                .menuId(IdGenerator.generateIntId(Integer.MAX_VALUE))
                .menuCategory(menuCategory)
                .menuName(menuName)
                .menuInfo(menuInfo)
                .allergy(allergy)
                .menuState(MenuState.UNSALE).build();

        menu.setMenuOptions(
                menuOptions.stream()
                        .map(dto -> dto.toEntity(menu))
                        .collect(Collectors.toList()));

        return menu;
    }
}
