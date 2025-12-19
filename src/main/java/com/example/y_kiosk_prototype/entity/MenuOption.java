package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.MenuOptionId;
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
public class MenuOption {
    @EmbeddedId
    private MenuOptionId menuOptionId;

    private String optionName;

    private int selectionNum;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @MapsId("menuId")
    private Menu menu;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menuOption", orphanRemoval = true)
    private List<OptionCategory> optionCategories = new ArrayList<>();

}
