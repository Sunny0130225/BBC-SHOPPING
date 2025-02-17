<script setup>
import HeadLogin from "@/views/HeadLogin.vue";
import { ref } from "vue";
</script>

<template>
  <div>
    <HeadLogin></HeadLogin>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">會員中心</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link to="/member/memberDetails">用戶詳細資料</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/member/changePassword"
                >更改登入資料
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/member/myOrder">我的訂單</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/member/myDiscount">我的優惠券</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<style lang="css" scoped>
.header {
  height: 56px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1em;
  background-color: #d9b3b3;
}
</style>
