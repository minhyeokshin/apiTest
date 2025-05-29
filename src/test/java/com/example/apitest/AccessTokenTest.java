package com.example.apitest;

import com.example.apitest.cache.TokenHolder;
import com.example.apitest.service.UcanSignService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AccessTokenTest {

    @Autowired
    private final UcanSignService ucanSignService;

    public AccessTokenTest() {
        ucanSignService = null;
    }

    @Test
    public void getAccessToken() {
        ucanSignService.sendTokenRequest();
        String token = TokenHolder.getToken();
        log.info("token : {}", token);

    }
}
