<script setup>
import { sendSign } from "@/services/signService";
import { reactive } from "vue";

const state = reactive({
  response: null
});

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

  const res = await sendSign(requestData);

  if (res.status === 200) {
    const signUrl = res.data; // 백엔드에서 받은 서명 URL
    window.location.href = signUrl; // 현재 페이지에서 리디렉션
  } else {
    alert("서명 요청 중 오류가 발생했습니다.");
  }
};


</script>

<template>
  <div>
    <button @click="requestSignature">서명 요청</button>
  </div>
 
</template>