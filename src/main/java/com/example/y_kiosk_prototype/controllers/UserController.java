package com.example.y_kiosk_prototype.controllers;

import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //@PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @GetMapping("/api/user/name")
    public ResponseEntity<Map<String, String>> getUserName(Authentication authentication) {
        log.info("UserController getUserData");

        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("userDetails: {}", userDetails);

        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        Map<String, String> result = new HashMap<>();
        result.put("name", userInfo.getName());

        return ResponseEntity.ok(result);
    }
}
