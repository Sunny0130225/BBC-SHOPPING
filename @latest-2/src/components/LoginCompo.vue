<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-light">
    <div class="card shadow p-4" style="width: 400px">
      <h3 class="text-center mb-4">BBC後臺登入</h3>
      <form @submit.prevent="handleLogin">
        <!-- Email -->
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input
            v-model="email"
            type="email"
            class="form-control"
            id="email"
            placeholder="name@example.com"
            required
          />
        </div>
        <!-- Password -->
        <div class="mb-3">
          <label for="password" class="form-label">密碼</label>
          <input
            v-model="password"
            type="password"
            class="form-control"
            id="password"
            placeholder="請輸入密碼"
            required
          />
        </div>
        <!-- Login Button -->
        <button type="submit" class="pinkbtn w-100 mb-3">登入</button>
        <p v-if="error" class="text-danger text-center">{{ error }}</p>
      </form>
      <!-- Add Member Button -->
      <button @click="createMember" class="pinkbtn w-100">註冊會員</button>
      <div style="text-align: center">
        <router-link to="/forget-password">忘記密碼?</router-link>
      </div>
    </div>
  </div>
  <!-- 之前 -->
</template>

<script setup>
import axios from "axios";
import $ from "jquery";
import Swal from "sweetalert2";
import { ref } from "vue";
import { useRouter } from "vue-router";

const email = ref("");
const password = ref("");
const error = ref("");
const router = useRouter();

const handleLogin = async () => {
  console.log(email.value);
  console.log(password.value);
  // 重置錯誤訊息
  error.value = "";
  // 登入邏輯
  const loginResp = await axios.post("/member/login", {
    email: email.value,
    passwords: password.value,
  });
  if (loginResp.data.success) {
    const detailsResp = await axios.get(
      `/member/searchDetails/${loginResp.data.memberId}`
    );

    if (detailsResp.data.role == null || detailsResp.data.role == "") {
      error.value = "此帳號沒有權限登入後台";
      console.log("此帳號沒有權限登入後台");
      return;
    }
    console.log("登入成功");
    console.log("會員 ID:", loginResp.data.memberId);
    console.log("會員名稱:", loginResp.data.username);

    // 保存會員資訊到本地存儲或狀態管理
    sessionStorage.setItem("isLoggedIn", "true"); // 記錄登入狀態
    sessionStorage.setItem("memberId", loginResp.data.memberId);
    sessionStorage.setItem("username", loginResp.data.username);
    sessionStorage.setItem("role", detailsResp.data.role);
    router.push("/index/memberView"); // 跳轉到登入後首頁
  } else {
    error.value = "帳號密碼錯誤";
  }
};

// 新增會員
const createMember = () => {
  Swal.fire({
    title: "請填入註冊相關訊息",
    html: `
          <table>
                <tr><td><label for="cuname">用戶名:</label></td>
                    <td>
                         <input type="text" id="cuname" class="swal2-input">
                </td></tr>
                <tr><td>
                         <label for="cemail">信箱:</label>
                  </td>
                  <td>
                        <input type="text" id="cemail" class="swal2-input" " >
                  </td></tr>
                  <tr><td>
                          <label for="cpassword">密碼:</label>
                     </td>
                      <td>
                          <input type="password" id="cpassword" class="swal2-input" " >
                  </td></tr>
           </table>

          `,
    focusConfirm: false,
    showCancelButton: true,
    confirmButtonText: "註冊",
    preConfirm() {
      return {
        username: $("#cuname").val(),
        email: $("#cemail").val(),
        passwords: $("#cpassword").val(),
      };
    },
  }).then((result) => {
    if (result.isConfirmed) {
      axios
        .post("/front/insert", result.value)
        .then((resp) => {
          if (resp.data == true) {
            Swal.fire("成功", "用戶資料已註冊", "success");
          } else {
            Swal.fire("失敗", "已有此用戶email，請使用其他信箱註冊", "error");
          }
        })
        .catch((err) => {
          Swal.fire("錯誤", "創建遇到錯誤", "warning");
        });
    }
  });
};
</script>

<style scoped>
/* 樣式省略 */
</style>
