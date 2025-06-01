package com.example.apitest.controller;

import com.example.apitest.dto.SignRequestDTO;
import com.example.apitest.cache.TokenHolder;
import com.example.apitest.service.UcanSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private final UcanSignService ucanSignService;

    public TestController(UcanSignService ucanSignService) {
        this.ucanSignService = ucanSignService;
    }

    // access token 발급테스트
    @GetMapping("/test")
    public String index() {
        ucanSignService.sendTokenRequest();
        String token = TokenHolder.getToken();
        return token;
    }

    // 템플릿 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<String> list() throws IOException {
        String result = ucanSignService.searchList();
        return ResponseEntity.ok(result);
    }

    // 서명 요청 테스트
    @PostMapping("/sign/test")
    public ResponseEntity<String>sendTestSign(){
        String response = ucanSignService.signRequest();
        return ResponseEntity.ok(response);

    }

    // 문서 커스텀 테스트
    @PostMapping("/documents/custom")
    public ResponseEntity<String>sendCustomSign(){
        String response = ucanSignService.signRequest();
        return ResponseEntity.ok(response);

    }

    // 전자서명 페이지 생성 테스트
    @PostMapping("/create")
    public ResponseEntity<String>createSignPage(@RequestBody SignRequestDTO signRequestDTO){
        try {
            String signPageUrl = ucanSignService.createSignRequest(signRequestDTO);
            return ResponseEntity.ok(signPageUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서명 생성 실패: " + e.getMessage());
        }

    }


}
