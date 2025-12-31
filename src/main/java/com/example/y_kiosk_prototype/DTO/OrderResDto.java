package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.data.OrderState;
import com.example.y_kiosk_prototype.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResDto {
    private int orderNum;
    private LocalDateTime orderTime;
    private OrderState orderState;
    private List<OrderedMenuResDto> orderedMenuResDtos;

    public static OrderResDto from(Order order) {
        if (order.getOrderState() != OrderState.ORDERED && order.getOrderState() != OrderState.COOKING && order.getOrderState() != OrderState.READY) {
            return null;
        }

        OrderResDto orderResDto = OrderResDto.builder()
                .orderNum(order.getOrderId().getOrderNum())
                .orderTime(order.getOrderId().getOrderTime())
                .orderState(order.getOrderState())
                .orderedMenuResDtos(order.getOrderedMenus() == null ? Collections.emptyList() :
                        order.getOrderedMenus()
                                .stream()
                                .map(OrderedMenuResDto::from)
                                .toList())
                .build();
        return orderResDto;
    }
}
