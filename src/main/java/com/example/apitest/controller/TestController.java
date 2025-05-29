package com.example.apitest.controller;

import com.example.apitest.cache.TokenHolder;
import com.example.apitest.service.UcanSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UcanSignService ucanSignService;

    // access token 발급테스트
    @GetMapping("/api/test")
    public String index() {
        String token = TokenHolder.getToken();
        return token;
    }

}
