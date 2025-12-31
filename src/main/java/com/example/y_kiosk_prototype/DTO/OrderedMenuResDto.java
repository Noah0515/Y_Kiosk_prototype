package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.entity.OrderedMenu;
import com.example.y_kiosk_prototype.entity.OrderedMenuOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderedMenuResDto {
    private int menuId;
    private String menuName;
    private int quantity;
    private List<OrderedMenuOptionResDto> orderedMenuOptions;

    public static OrderedMenuResDto from(OrderedMenu orderedMenu) {
        OrderedMenuResDto orderedMenuResDto = OrderedMenuResDto.builder()
                .menuId(orderedMenu.getOrderedMenuId().getMenuId())
                .menuName(orderedMenu.getMenu().getMenuName())
                .quantity(orderedMenu.getQuantity())
                .orderedMenuOptions(orderedMenu.getOrderedMenuOptions() == null ? Collections.emptyList() :
                        orderedMenu.getOrderedMenuOptions()
                                .stream()
                                .map(OrderedMenuOptionResDto::from)
                                .toList())
                .build();

        return orderedMenuResDto;

    }
}
