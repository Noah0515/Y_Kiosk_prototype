package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.data.StoreState;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    private String storeId; // 16자리

    //private String userId;

    private String storeName;

    private String verifyCode;

    @Enumerated(EnumType.STRING)
    private StoreState state;

    private LocalDate create_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<EmployeePosition> employeePositions = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<MenuGroup> menuGroups = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

}
