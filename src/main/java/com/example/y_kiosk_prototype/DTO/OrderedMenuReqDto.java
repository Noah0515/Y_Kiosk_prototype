package com.example.y_kiosk_prototype.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderedMenuReqDto {
    private int menuId;
    private int quantity;
    private List<OrderedMenuOptionReqDto> orderedMenuOptions;
}
