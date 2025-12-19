package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.OptionCategoryId;
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
public class OptionCategory {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "menuId", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "optionId", column = @Column(name = "option_id")),
            @AttributeOverride(name = "categoryId", column = @Column(name = "category_id"))
    })
    private OptionCategoryId optionCategoryId;

    private String optionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "menu_id", insertable = false, updatable = false),
            @JoinColumn(name = "option_id", insertable = false, updatable = false)
    })
    private MenuOption menuOption;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "optionCategory", orphanRemoval = true)
    private List<OrderedMenuOption> orderedMenuOptions = new ArrayList<>();
}
