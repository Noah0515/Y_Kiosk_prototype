package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.OrderedMenuOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderedMenuOptionResDto {
    private String optionCategoryName;
    private String optionContent;

    public static OrderedMenuOptionResDto from(OrderedMenuOption option) {
        OrderedMenuOptionResDto orderedMenuOptionResDto = OrderedMenuOptionResDto.builder()
                .optionCategoryName(option.getOptionCategory().getMenuOption().getOptionName())
                .optionContent(option.getOptionCategory().getOptionContent())
                .build();

        return orderedMenuOptionResDto;

    }
}
