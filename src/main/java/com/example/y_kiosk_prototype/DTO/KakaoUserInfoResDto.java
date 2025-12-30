package com.example.y_kiosk_prototype.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResDto {
    private Long id; // (kakaoId)

    // 추가 정보(이메일, 닉네임 등)가 필요하면 아래처럼 정의하세요.
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private String email;
    }
}
