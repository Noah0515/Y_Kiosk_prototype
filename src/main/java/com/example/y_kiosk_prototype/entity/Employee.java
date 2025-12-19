package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.EmployeePositionId;
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
public class Employee {
    @Id
    private String employeeId;
    //private String userId;
    //private String storeId;
    //private int position;
    private String employeeNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "store_id"),
            @JoinColumn(name = "position")
    })
    private EmployeePosition employeePosition;
}
