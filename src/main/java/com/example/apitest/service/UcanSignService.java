package com.example.apitest.service;

import com.example.apitest.dto.SignRequestDTO;
import com.example.apitest.cache.TokenHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class UcanSignService {

    @Value("${uCanSignKey}")
    private String apiKey;

    private static final String API_URL = "https://app.ucansign.com/openapi/embedding/sign-creating";
    private final OkHttpClient httpClient = new OkHttpClient();

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

    // 템플릿 리스트 조회
    public String searchList() throws IOException {
        String token = TokenHolder.getToken();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://app.ucansign.com/openapi/templates")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-ucansign-test", "true")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string(); // ✅ 응답 JSON 문자열 반환
            } else {
                throw new RuntimeException("UCanSign 요청 실패: " + response.code());
            }
        }


    }


    // 서명 요청
    public String signRequest() {
        OkHttpClient client = new OkHttpClient();
        String templateId = "1927294313194180609";
        String token = TokenHolder.getToken();

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\n" +
                "  \"participants\": [\n" +
                "    {\n" +
                "      \"name\": \"홍길동\",\n" +
                "      \"signingMethodType\": \"kakao\",\n" +
                "      \"signingContactInfo\": \"01012345678\",\n" +
                "      \"signingOrder\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"김유캔\",\n" +
                "      \"signingMethodType\": \"email\",\n" +
                "      \"signingContactInfo\": \"abcd@email.com\",\n" +
                "      \"signingOrder\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("https://app.ucansign.com/openapi/templates/" + templateId)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-ucansign-test", "true")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);

                // Step 1️⃣: 문서 생성 후 documentId 추출
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                String documentId = rootNode.path("result").path("documentId").asText();

                if (documentId == null || documentId.isEmpty()) {
                    throw new RuntimeException("documentId를 찾을 수 없습니다!");
                }

                // Step 2️⃣: signUrl 조회 전에 2초 대기
                Thread.sleep(2000);

                // Step 3️⃣: documentId로 서명 URL 조회
                String signUrl = getSignUrl(documentId, token);
                System.out.println("✅ signUrl = " + signUrl);
                return signUrl;

            } else {
                throw new RuntimeException("UCanSign 문서 생성 실패: " + response.code() + "\n" + response.body().string());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    // 문서 접근 페이지 요청
    private String getSignUrl(String documentId, String token) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        System.out.println(documentId);
        Long id = Long.parseLong(documentId);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n" +
            "    \"participantId\": \"\", \r\n" +
            "    \"redirectUrl\": \"http://localhost.com/5173\"\r\n" +
            "}");
        Request request = new Request.Builder()
                .url("https://app.ucansign.com/openapi/embedding/view/" + id)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-ucansign-test", "true") // 테스트용
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                System.out.println(rootNode.path("result").path("url").asText());
                System.out.println("리다이렉트 url");
                System.out.println(rootNode.path("url"));
                return rootNode.path("result").path("url").asText();
            } else {
                throw new RuntimeException("UCanSign 문서 조회 실패: " + response.code() + "\n" + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    //진짜 서명 페이지 요청
    public String createSignRequest(SignRequestDTO signRequestDTO) {
        String bearerToken = TokenHolder.getToken();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBodyJson = String.format(
                "{\"redirectUrl\":\"%s\",\"customValue\":\"%s\"}",
                signRequestDTO.getRedirectUrl(),
                signRequestDTO.getCustomValue() != null ? signRequestDTO.getCustomValue() : ""
        );

        RequestBody body = RequestBody.create(mediaType, requestBodyJson);

        Request request = new Request.Builder()
                .url(API_URL)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-ucansign-test", "true")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);
                int code = root.path("code").asInt();

                if (code == 0) {
                    String url = root.path("result").path("url").asText();
                    return url;
                } else {
                    throw new RuntimeException("유캔사인 API 호출 실패: " + root.path("msg").asText());
                }
            } else {
                throw new RuntimeException("유캔사인 API 호출 실패 (HTTP " + response.code() + ")");
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }




}
