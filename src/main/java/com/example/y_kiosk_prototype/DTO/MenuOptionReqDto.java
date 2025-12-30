package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.composite_key.MenuOptionId;
import com.example.y_kiosk_prototype.entity.Menu;
import com.example.y_kiosk_prototype.entity.MenuOption;
import com.example.y_kiosk_prototype.entity.OptionCategory;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuOptionReqDto {
    private String optionName;
    private int selectionNum;
    private List<OptionCategoryReqDto> optionCategories;

    public MenuOption toEntity(Menu menu) {
        MenuOption menuOption = MenuOption.builder()
                .menu(menu)
                .menuOptionId(MenuOptionId
                        .builder()
                        .menuId(menu.getMenuId())
                        .optionId(IdGenerator.generateIntId(Integer.MAX_VALUE))
                        .build())
                .optionName(optionName)
                .selectionNum(selectionNum)
                .build();

        menuOption.setOptionCategories(
                optionCategories.stream()
                .map(dto -> dto.toEntity(menuOption))
                .collect(Collectors.toList()));

        return menuOption;
    }
}
