package com.example.y_kiosk_prototype.controllers;

import com.example.y_kiosk_prototype.DTO.KakaoUserInfoResDto;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.login.JwtTokenProvider;
import com.example.y_kiosk_prototype.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(Authentication authentication, HttpServletResponse response) {
        log.info("Logout {}", userService.findUserByUserId(((UserDetails) authentication.getPrincipal()).getUsername()));
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("로그아웃");
    }

    @PostMapping("/api/auth/kakao/android")
    public ResponseEntity<?> authenticateKakaoAndroid(@RequestBody Map<String, String> request) {
        log.info("Authenticate Kakao Android");
        String kakaoAccessToken = request.get("accessToken");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 1. 카카오 정보 획득
            ResponseEntity<KakaoUserInfoResDto> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserInfoResDto.class
            );

            KakaoUserInfoResDto info = response.getBody();

            // 2. 서비스 로직 호출 (DB 저장 및 조회)
            UserInfo userInfo = userService.findOrCreateKakaoUser(
                    info.getId(),
                    info.getKakaoAccount().getEmail()
            );

            // 3. 사용자님의 JwtTokenProvider로 토큰 생성
            String ourJwt = jwtTokenProvider.createAccessToken(userInfo);

            // 4. 응답 전송
            return ResponseEntity.ok(Map.of(
                    "accessToken", ourJwt,
                    "userId", userInfo.getUserId(),
                    "userName", userInfo.getName()
            ));

        } catch (Exception e) {
            log.error("인증 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
