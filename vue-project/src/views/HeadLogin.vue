<template>
  <header class="header">
    <router-link to="/loginHome" class="logo"
      ><img src="http://localhost:8081/upload/B1.png" class="logoPic" /><span
        style="font-size: 25px; margin-top: 3px"
        >BBC購物</span
      ></router-link
    >
    <nav>
      <img :src="photoUrl" alt="會員照片" class="member-photo member" />
      <router-link to="/member" class="member">會員中心</router-link>
      <router-link to="/cart">購物車</router-link>
      <router-link to="/" @click="handleLogout">登出</router-link>
    </nav>
  </header>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";

const handleLogout = () => {
  // 清除 localStorage
  sessionStorage.removeItem("isLoggedIn");
  sessionStorage.removeItem("memberId");
  sessionStorage.removeItem("username");
};

// 頭像
let photoUrl = ref(`${axios.defaults.baseURL}/upload/default.jpg`); // 從後端獲取的會員照片 URL
onMounted(() => {
  axios
    .get(`/front/getImg/${sessionStorage.getItem("memberId")}`)
    .then((resp) => {
      photoUrl.value =
        resp.data || `${axios.defaults.baseURL}/upload/default.jpg`;
    })
    .catch((err) => {
      console.log("獲取圖片失敗" + err);
      photoUrl.value = `${axios.defaults.baseURL}/upload/default.jpg`;
    });
});
</script>

<style>
.header {
  font-size: 20px;
  font-weight: 700;
  height: 80px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  background-color: #d9b3b3;
}
nav a {
  margin: 0 1em;
  text-decoration: none;
  color: #333;
}
.member {
  margin: 0px;
  text-decoration: none;
  color: #333;
}

nav a:hover {
  color: #0a3f6165;
}
.member-photo {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}
.logo {
  color: black;
  text-decoration: none;
}
.logoPic {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
</style>
