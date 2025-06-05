package com.example.apitest.controller;

import com.example.apitest.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OpenAiController {
    private final OpenAiService openAiService;

//    @GetMapping("/openai")
//    public String openAi() throws IOException {
//        return openAiService.callOpenAi();
//    }

    @GetMapping("/openAiTest")
    public ResponseEntity<String> callOpenAi() throws IOException {
        return ResponseEntity.ok(openAiService.callOpenAi());
    }

    @GetMapping("/openAiTest2")
    public ResponseEntity<String> callOpenAi2(String msg) throws IOException {
        return ResponseEntity.ok(openAiService.openAi2("ai/baseRequest.json",msg));
    }
}
