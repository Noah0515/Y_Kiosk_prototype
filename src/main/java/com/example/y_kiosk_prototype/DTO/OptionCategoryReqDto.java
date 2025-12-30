package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.composite_key.OptionCategoryId;
import com.example.y_kiosk_prototype.entity.MenuOption;
import com.example.y_kiosk_prototype.entity.OptionCategory;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionCategoryReqDto {
    private String optionContent;

    public OptionCategory toEntity(MenuOption menuOption) {
        OptionCategory optionCategory = OptionCategory.builder()
                .optionCategoryId(OptionCategoryId.builder()
                        .menuId(menuOption.getMenuOptionId().getMenuId())
                        .optionId(menuOption.getMenuOptionId().getOptionId())
                        .categoryId(IdGenerator.generateIntId(Integer.MAX_VALUE))
                        .build())
                .optionContent(optionContent)
                .build();

        return optionCategory;
    }
}
