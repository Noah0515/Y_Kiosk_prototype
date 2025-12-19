package com.example.y_kiosk_prototype.composite_key;

import com.example.y_kiosk_prototype.entity.OptionCategory;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class OrderedMenuOptionId implements Serializable {
    private OrderedMenuId orderedMenuId;
    private OptionCategoryId optionCategoryId;
    /*
    private int orderNum;
    private int menuId;
    private int catMenuId; // menuId가 중복되어서 JPA 충돌을 피하기 위한 column
    private LocalDateTime orderTime;
    private String storeId;
    private int optionId;
    private int categoryId;
    */
    public OrderedMenuOptionId() {}


    @Override
    public String toString() {
        return super.toString();
    }
}
