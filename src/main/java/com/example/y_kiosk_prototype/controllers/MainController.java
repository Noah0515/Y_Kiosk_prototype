package com.example.y_kiosk_prototype.controllers;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class MainController {
    public MainController() {}

    @GetMapping("/main")
    public String getMainPage(){
        log.info("MainController.getMainPage");
        return "main";
    }

    @GetMapping("/")
    public String getDefaultPage(){
        log.info("MainController.getDefaultPage");
        return "main";
    }

    @PostMapping("/oauth2/kakao_login")
    public String kakaoLoginRedirect(){
        log.info("MainController.kakaoLoginRedirect");
        return "";
    }

    /*
    @GetMapping("/login/required")
    public String loginRequired(){
        log.info("MainController.loginRequired");
        return "required-page";
    }*/
}
