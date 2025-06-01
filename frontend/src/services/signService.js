import httpRequester from "@/libs/httpRequester";

// 서명 요청 테스트
export const sendSign = (requestData) => {
return httpRequester.post("/api/sign/test", requestData).catch(e => e.response);

}

// 전자 서명 페이지 생성 요청
export const createSignPage = async(requestData) => {
 try {
  const response = await httpRequester.post("/api/create", requestData);
  return response;} catch (error) {
 if (error.response) {
return error.response;} else { throw error;}}


};
