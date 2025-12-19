package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.data.MenuState;
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
public class Menu {
    @Id
    private int menuId;

    //private int menuCategoryId;

    private String menuName;

    private String menuInfo;

    private String allergy;

    @Enumerated(EnumType.STRING)
    private MenuState menuState;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menu", orphanRemoval = true)
    private List<OrderedMenu> orderedMenus = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menu", orphanRemoval = true)
    private List<MenuOption> menuOptions = new ArrayList<>();

}
