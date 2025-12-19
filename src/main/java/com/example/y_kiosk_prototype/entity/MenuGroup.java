package com.example.y_kiosk_prototype.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroup {
    @Id
    private int menuGroupId;

    //private String storeId;

    private String menuGroupName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menuGroup", orphanRemoval = true)
    private List<MenuCategory> menuCategories = new ArrayList<>();

}
