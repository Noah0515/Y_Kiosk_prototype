package com.example.y_kiosk_prototype.entity;

import com.example.y_kiosk_prototype.composite_key.UsersId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    @EmbeddedId
    private UsersId usersId;
    private String pw;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;


}
