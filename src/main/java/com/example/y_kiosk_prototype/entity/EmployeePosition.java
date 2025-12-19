package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.EmployeePositionId;
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
public class EmployeePosition {
    @EmbeddedId
    private EmployeePositionId employeePositionId;

    private String positionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employeePosition", orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();


}
