package com.example.y_kiosk_prototype.composite_key;

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
public class OrderedMenuId implements Serializable {
    private int orderNum;
    private int menuId;
    private LocalDateTime orderTime;
    private String storeId;

    public OrderedMenuId() {}

    @Override
    public String toString() {
        return super.toString();
    }
}
