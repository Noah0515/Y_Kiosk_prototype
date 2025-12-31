package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OrderedMenuOptionId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderedMenuOption {
    @EmbeddedId
    @AttributeOverrides({
            // OptionCategory 관련 컬럼 매핑 재정의
            @AttributeOverride(name = "optionCategoryId.menuId", column = @Column(name = "cat_menu_id")),
            @AttributeOverride(name = "optionCategoryId.optionId", column = @Column(name = "option_id")),
            @AttributeOverride(name = "optionCategoryId.categoryId", column = @Column(name = "category_id")),

            // OrderedMenu 관련 컬럼 매핑 재정의 (필요시)
            @AttributeOverride(name = "orderedMenuId.orderNum", column = @Column(name = "order_num")),
            @AttributeOverride(name = "orderedMenuId.menuId", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "orderedMenuId.orderTime", column = @Column(name = "order_time")),
            @AttributeOverride(name = "orderedMenuId.storeId", column = @Column(name = "store_id")),
            @AttributeOverride(name = "orderedMenuId.orderedMenuSeq", column = @Column(name = "ordered_menu_seq")) // 💡 순번 추가됨
    })
    private OrderedMenuOptionId orderedMenuOptionId;

    private String optionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("optionCategoryId")
    @JoinColumns({
            @JoinColumn(name = "cat_menu_id", referencedColumnName = "menu_id"),
            @JoinColumn(name = "option_id", referencedColumnName = "option_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    })
    private OptionCategory optionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "order_num", referencedColumnName = "order_num"),
            @JoinColumn(name = "menu_id", referencedColumnName = "menu_id"),
            @JoinColumn(name = "order_time", referencedColumnName = "order_time"),
            @JoinColumn(name = "store_id", referencedColumnName = "store_id"),
            // 💡 부모의 새로운 키인 seq를 여기서도 연결해줘야 합니다!
            @JoinColumn(name = "ordered_menu_seq", referencedColumnName = "ordered_menu_seq")
    })
    @MapsId("orderedMenuId")
    private OrderedMenu orderedMenu;
}
