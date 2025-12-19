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
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class UsersId implements Serializable {
    private String loginId;
    private String userId;

    public UsersId() {

    }

    @Override
    public String toString() {
        return "";
    }
}
