package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.data.OrderState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`orders`")
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

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderedMenu> orderedMenus = new ArrayList<>();

    @Column(columnDefinition = "integer default 0") // DB 생성 시 기본값 0 지정
    private int version = 0;
}
