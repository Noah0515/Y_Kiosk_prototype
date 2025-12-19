package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderedMenu {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "orderNum", column = @Column(name = "order_num")),
            @AttributeOverride(name = "orderTime", column = @Column(name = "order_time")),
            @AttributeOverride(name = "storeId", column = @Column(name = "store_id")),
            @AttributeOverride(name = "menuId", column = @Column(name = "menu_id"))
    })
    private OrderedMenuId orderedMenuId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "order_num", insertable = false, updatable = false),
            @JoinColumn(name = "order_time", insertable = false, updatable = false),
            @JoinColumn(name = "store_id", insertable = false, updatable = false)
    })
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderedMenu", orphanRemoval = true)
    private List<OrderedMenuOption> orderedMenuOptions = new ArrayList<>();

}
