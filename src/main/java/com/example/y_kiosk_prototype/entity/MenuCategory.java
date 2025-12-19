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
public class MenuCategory {
    @Id
    private int menuCategoryId;

    //private int menuGroupId;

    private String menuCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menuCategory", orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

}
