package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuOptionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderedMenuOption {
    @EmbeddedId
    @AttributeOverride(
            name = "optionCategoryId.menuId",
            column = @Column(name = "cat_menu_id")
    )
    private OrderedMenuOptionId orderedMenuOptionId;

    //private int optionId;

    //private int categoryId;

    private String optionContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cat_menu_id"),
            @JoinColumn(name = "option_id"),
            @JoinColumn(name = "category_id")
    })
    @MapsId("optionCategoryId")
    private OptionCategory optionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "order_num"),
            @JoinColumn(name = "menu_id"),
            @JoinColumn(name = "order_time"),
            @JoinColumn(name = "store_id")
    })
    @MapsId("orderedMenuId")
    private OrderedMenu orderedMenu;
}
