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
public class EmployeePositionId implements Serializable {
    private String storeId;
    private int position;

    public EmployeePositionId() {}

    @Override
    public String toString() {
        return super.toString();
    }
}
