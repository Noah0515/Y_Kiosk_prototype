package com.example.y_kiosk_prototype.controllers;

import com.example.y_kiosk_prototype.DTO.StoreReqDto;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.service.StoreService;
import com.example.y_kiosk_prototype.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;

    @GetMapping("/store")
    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    public String getStorePage() {
        return "store";
    }

    @PreAuthorize("hasRole('NORMAL')")
    @PostMapping("/api/user/store/create")
    public ResponseEntity<String> createStore(@RequestBody StoreReqDto storeReqDto, Authentication authentication) {
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("storeName: {}", storeReqDto.getStoreName());
        log.info("userDetails: {}", userDetails);

        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        Store newStore = storeService.createStore(storeReqDto, userInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(newStore.getStoreId());
    }

}
