<template>
  <header class="header">
    <router-link to="/loginHome" class="logo"
      ><img src="http://localhost:8081/upload/B1.png" class="logoPic" /><span
        style="font-size: 25px; margin-top: 3px"
        >BBC購物</span
      ></router-link
    >
    <nav>
      <router-link to="/login">登入</router-link>
    </nav>
  </header>
</template>

<script setup></script>

<style>
.header {
  font-size: 20px;
  font-weight: 700;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1em;
  background-color: #d9b3b3;
}
nav a {
  margin: 0 1em;
  text-decoration: none;
  color: #333;
}
nav a:hover {
  color: #007bff;
}
</style>
