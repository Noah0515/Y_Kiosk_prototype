package com.example.y_kiosk_prototype.composite_key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class OptionCategoryId implements Serializable {
    private int menuId;
    private int optionId;
    private int categoryId;

    public OptionCategoryId() {}


    @Override
    public String toString() {
        return super.toString();
    }
}
