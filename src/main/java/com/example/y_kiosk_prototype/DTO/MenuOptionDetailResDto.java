package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.MenuOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class MenuOptionDetailResDto {
    private String optionName;
    private int optionId;
    private int selectionNum;
    private List<OptionCategoryDetailResDto> optionCategoryDetailResDtoList;

    public static MenuOptionDetailResDto from(MenuOption menuOption) {
        MenuOptionDetailResDto menuOptionDetailResDto = MenuOptionDetailResDto.builder()
                .optionName(menuOption.getOptionName())
                .optionId(menuOption.getMenuOptionId().getOptionId())
                .selectionNum(menuOption.getSelectionNum())
                .optionCategoryDetailResDtoList(menuOption.getOptionCategories() == null ? Collections.emptyList() :
                        menuOption.getOptionCategories()
                                .stream()
                                .map(OptionCategoryDetailResDto::from)
                                .toList())
                .build();

        return menuOptionDetailResDto;
    }

}
