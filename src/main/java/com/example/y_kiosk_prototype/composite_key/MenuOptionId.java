package com.example.y_kiosk_prototype.composite_key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class MenuOptionId implements Serializable {
    private int menuId;
    private int optionId;

    public MenuOptionId() {}


    @Override
    public String toString() {
        return super.toString();
    }
}
