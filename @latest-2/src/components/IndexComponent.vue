<script setup>
import { ref } from "vue";

const username = ref(sessionStorage.getItem("username"));
const memberId = ref(sessionStorage.getItem("memberId"));
const role = ref(sessionStorage.getItem("role"));
const manager = role.value === "admin" ? "管理者" : "最高管理者";
const handleLogout = () => {
  // 清除 localStorage
  sessionStorage.removeItem("isLoggedIn");
  sessionStorage.removeItem("memberId");
  sessionStorage.removeItem("username");
  sessionStorage.removeItem("role");
};
</script>

<template>
  <div>
    <div id="function">
      <span style="font-size: 30px; display: inline; padding: 10px"
        >BBC管理後臺</span
      >
      <span style="display: flex; justify-content: flex-end; padding-top: 5px">
        <span style="margin: 8px">您好，{{ username }}({{ manager }})</span>
        <router-link
          to="/"
          @click="handleLogout"
          style="color: white; text-decoration: none; height: 30px; margin: 8px"
          >登出</router-link
        >
      </span>
    </div>

    <nav class="box">
      <router-link
        to="/index/memberView"
        style="color: white; text-decoration: none"
        class="botten"
        >會員管理</router-link
      >
      <router-link
        to="/index/productView"
        style="color: white; text-decoration: none"
        class="botten"
        >商品管理</router-link
      >
      <router-link
        to="/index/ordersView"
        style="color: white; text-decoration: none"
        class="botten"
        >訂單管理</router-link
      >
      <a href="http://localhost:8081/discounts" class="botten">折扣管理</a>
      <a href="http://localhost:8081/applyDiscount2" class="botten">折扣應用</a>
    </nav>
    <router-view />
  </div>
</template>

<style lang="css" scoped></style>
