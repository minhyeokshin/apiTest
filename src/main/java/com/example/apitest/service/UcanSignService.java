package com.example.apitest.service;

import com.example.apitest.cache.TokenHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UcanSignService {

    @Value("${uCanSignKey}")
    private String apiKey;

    @Scheduled(initialDelay = 0, fixedRate = 1200000) // 20분 = 1,200,000ms
    public void sendTokenRequest() {


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"apiKey\": \"" +apiKey+"\"\r\n}");
        Request request = new Request.Builder()
                .url("https://app.ucansign.com/openapi/user/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body().string());
            String token = root.path("result").path("accessToken").asText();
            TokenHolder.setToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
