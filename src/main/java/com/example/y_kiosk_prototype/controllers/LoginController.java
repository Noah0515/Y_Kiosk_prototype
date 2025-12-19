package com.example.y_kiosk_prototype.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {
    @GetMapping("/login/oauth2/kakao")
    public String kakaoLoginRedirect(){
        return "/login-redirect";
    }
}
