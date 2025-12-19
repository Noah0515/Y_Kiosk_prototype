package com.example.y_kiosk_prototype.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreController {

    @GetMapping("/store")
    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    public String getStorePage() {
        return "store";
    }
}
