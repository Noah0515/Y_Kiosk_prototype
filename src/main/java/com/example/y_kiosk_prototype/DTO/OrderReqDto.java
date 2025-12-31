package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.data.OrderState;
import com.example.y_kiosk_prototype.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderReqDto {
    private String storeId;
    private List<OrderedMenuReqDto> orderedMenus;

    /*
    public Order toEntity(int orderNum) {
        Order order = Order.builder()
                .orderId(OrderId.builder()
                        .orderNum(orderNum)
                        .orderTime(LocalDateTime.now())
                        .storeId(storeId)
                        .build())
                .orderState(OrderState.ORDERED)
                .build();
    }*/
}
