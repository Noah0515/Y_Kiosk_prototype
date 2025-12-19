package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.data.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {
    // db에 mapping되는 변수
    @Id
    private String userId;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private UserType userType;

    private LocalDate createAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userInfo", orphanRemoval = true, optional = false)
    private UserAccount userAccount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userInfo", orphanRemoval = true, optional = false)
    private SocialLogin socialLogin;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userInfo", orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userInfo", orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    public String updateName(String newName){
        this.name = newName;
        return this.name;
    }

    @Override
    public String toString() {
        return "userId: " + userId + ", name: " + name + ", userType: " + userType + ", createAt: " + createAt;
    }
}
