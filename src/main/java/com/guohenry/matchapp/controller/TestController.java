package com.guohenry.matchapp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "你已成功通過 JWT 驗證！Hello！";
    }
}
