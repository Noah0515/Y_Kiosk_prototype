package com.example.y_kiosk_prototype.controllers;

import com.example.y_kiosk_prototype.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class LoginController {
    private final UserService userService;

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
}
