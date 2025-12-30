package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.OptionCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OptionCategoryDetailResDto {
    private int categoryId;
    private String optionContent;

    public static OptionCategoryDetailResDto from(OptionCategory optionCategory) {
        OptionCategoryDetailResDto optionCategoryDetailResDto = OptionCategoryDetailResDto.builder()
                .categoryId(optionCategory.getOptionCategoryId().getCategoryId())
                .optionContent(optionCategory.getOptionContent())
                .build();

        return optionCategoryDetailResDto;
    }

}
