<template>
  <div class="position">
    <h1 style="margin: 0px">忘記密碼</h1>
    <input
      v-model="email"
      type="email"
      placeholder="輸入您的電子郵件"
      class="form-control"
      style="width: 500px; display: inline"
      required
    />
    <button @click="submitEmail" class="button">發送重設密碼郵件</button>
    <p v-if="message" :class="{ error: isError, success: !isError }">
      {{ message }}
    </p>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";
import Swal from "sweetalert2";

let email = ref("");
let message = ref("");
let isError = ref(false);

const submitEmail = async () => {
  if (!email.value) {
    message.value = "請輸入電子郵件";
    isError.value = true;
    return;
  }

  try {
    const response = await axios.post("/api/auth/forgot-password", {
      email: email.value,
    });

    if (response.data.success) {
      Swal.fire("成功", "請檢查您的信箱，重設密碼郵件已發送！", "success");
      message.value = "郵件已發送，請檢查您的信箱";
      isError.value = false;
    }
  } catch (error) {
    if (error.response && error.response.status === 400) {
      message.value = error.response.data.message; // 後端傳回 "該 Email 不存在"
    } else {
      message.value = "無法發送郵件，請稍後再試。";
    }
    isError.value = true;
  }
};
</script>

<style scoped>
.position {
  margin: 100px;
  text-align: center;
}

.error {
  color: red;
}

.success {
  color: green;
}
</style>
