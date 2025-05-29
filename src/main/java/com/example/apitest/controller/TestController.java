package com.example.apitest.controller;

import com.example.apitest.cache.TokenHolder;
import com.example.apitest.service.UcanSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private final UcanSignService ucanSignService;

    public TestController(UcanSignService ucanSignService) {
        this.ucanSignService = ucanSignService;
    }

    // access token 발급테스트
    @GetMapping("/api/test")
    public String index() {
        ucanSignService.sendTokenRequest();
        String token = TokenHolder.getToken();
        return token;
    }

}
