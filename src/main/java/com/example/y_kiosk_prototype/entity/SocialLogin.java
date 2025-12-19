package com.example.y_kiosk_prototype.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLogin {
    @Id
    private String userId;
    private String kakaoEmail;
    private String kakaoId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @Override
    public String toString() {
        return "kakaoEmail: " + kakaoEmail + ", kakaoId: " + kakaoId;
    }
}
