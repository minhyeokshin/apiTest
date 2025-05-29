package com.example.apitest.cache;


import lombok.Getter;

public class TokenHolder {
    @Getter
    private volatile static String token;

    public static void setToken(String token) {
        TokenHolder.token = token;
    }
}
