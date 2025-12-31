package com.example.y_kiosk_prototype.DTO;

import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.data.OrderState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderStatusUpdateReqDto {
    private int orderNum;
    private LocalDateTime orderTime;
    private OrderState orderState;
    private String storeId;

    public OrderId getOrderId() {
        return OrderId.builder()
                .orderNum(orderNum)
                .orderTime(orderTime)
                .storeId(storeId)
                .build();
    }

}
