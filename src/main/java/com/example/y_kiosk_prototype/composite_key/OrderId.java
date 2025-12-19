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
public class OrderId implements Serializable {
    private int orderNum;
    private LocalDateTime orderTime;
    private String storeId;

    public OrderId(){}

    @Override
    public String toString() {
        return "";
    }
}
