package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.data.OrderState;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @EmbeddedId
    private OrderId orderId;

    //private int menuGroupId;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @MapsId("storeId")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
}
