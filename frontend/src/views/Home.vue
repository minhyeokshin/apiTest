<template>
                <div class="container mt-5">
                  <h1>전자서명 테스트 페이지</h1>
                  <button class="btn btn-primary me-2" @click="requestSignature">계약서 보기</button>
                  <button class="btn btn-success" @click="createSignature">서명 요청 페이지</button>
                </div>
              </template>
              
              <script setup>
              import { sendSign } from "@/services/signService";
              import { createSignPage } from "@/services/signService";
              
              const requestSignature = async () => {
                const requestData = {
                  documentName: "테스트문서_TEST",
                  folderId: 1927291533385650178,
                  configExpireMinute: 20160,
                  documentPassword: "abcd1234!@#$",
                  configExpireReminderDay: 3,
                  reservationDate: "2024-06-14 09:00:00",
                  fields: [
                    { fieldName: "날짜", value: "2024년 06월 01일" },
                    { fieldName: "계약물품", value: "모니터" },
                    { fieldName: "금액", value: "300,000" }
                  ],
                  participants: [
                    {
                      name: "홍길동",
                      signingMethodType: "kakao",
                      signingContactInfo: "01012345678",
                      signingOrder: 1
                    },
                    {
                      name: "김유캔",
                      signingMethodType: "email",
                      signingContactInfo: "abcd@email.com",
                      signingOrder: 2
                    }
                  ]
                };
              
                try {
                  const res = await sendSign(requestData);
                  if (res && res.status === 200) {
                    window.location.href = res.data;
                  } else {
                    alert("서명 요청 중 오류가 발생했습니다.");
                  }
                } catch (error) {
                  alert("서명 요청 중 네트워크 오류가 발생했습니다.");
                  console.error(error);
                }
              };
              
              const createSignature = async () => {
                const requestData = {
                  redirectUrl: "http://localhost:5173/redirect-complete",
                  customValue: "테스트값"
                };
              
                try {
                  const res = await createSignPage(requestData);
                  if (res && res.status === 200) {
                    window.location.href = res.data;
                  } else {
                    alert("서명 생성 요청 중 오류가 발생했습니다.");
                  }
                } catch (error) {
                  alert("서명 생성 요청 중 네트워크 오류가 발생했습니다.");
                  console.error(error);
                }
              };
              </script>
              
              <style scoped>
              .container {
                max-width: 600px;
                margin: 0 auto;
                text-align: center;
              }
              button {
                margin-top: 10px;
              }
              </style>