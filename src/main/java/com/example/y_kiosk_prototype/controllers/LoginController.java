package com.example.y_kiosk_prototype.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {


    @GetMapping("/login/oauth2/kakao")
    @PreAuthorize("hasAnyRole('NORMAL')")
    public String kakaoLoginRedirect(){
        log.info("kakaoLoginRedirect");

        return "login/login-redirect";
    }
}
