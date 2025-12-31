package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuOptionId;
import com.example.y_kiosk_prototype.entity.OrderedMenu;
import com.example.y_kiosk_prototype.entity.OrderedMenuOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedMenuOptionReqDto {
    private int optionId;
    private int categoryId;
    private String optionContent;

    /*
    public OrderedMenuOption toEntity(OrderedMenu orderedMenu) {
        OrderedMenuOption orderedMenuOption = OrderedMenuOption.builder()
                .orderedMenuOptionId(
                        OrderedMenuOptionId.builder()
                                .orderedMenuId(orderedMenu.getOrderedMenuId())
                                .optionCategoryId(categoryId)
                                .order
                                .build()
                )
                .build();
    }*/
}
