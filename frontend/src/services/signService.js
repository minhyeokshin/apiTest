import httpRequester from "@/libs/httpRequester";

// 서명 요청 테스트
export const sendSign = (requestData) => {
return httpRequester.post("/api/sign/test", requestData).catch(e => e.response);

}
