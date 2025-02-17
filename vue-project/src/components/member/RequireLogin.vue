<template>
  <div class="require-login-container">
    <h1>請先登入</h1>
    <p>您需要登入才能繼續訪問該頁面。</p>
    <p>正在跳轉到登入頁面...</p>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

onMounted(() => {
  // 等待 3 秒後跳轉到登入頁面
  setTimeout(() => {
    router.push({
      name: "login",
      query: { redirect: route.query.redirect || "/" }, // 存入原本想要去的頁面，之後登入直接取這個值跳轉到該頁面
    });
  }, 3000);
});
</script>

<style scoped>
.require-login-container {
  text-align: center;
  margin-top: 10%;
  font-family: Arial, sans-serif;
}
h1 {
  font-size: 2rem;
  color: #333;
}
p {
  margin: 1rem 0;
  color: #666;
}
</style>
