<template>
  <div>
    <HeadUnlogin></HeadUnlogin>
    <v-app>
      <v-main>
        <v-container>
          <v-carousel v-model="active" interval="3000" cycle hide-delimiters>
            <v-carousel-item
              v-for="(item, i) in imgs"
              :key="i"
              :src="item.image"
            >
              <v-row class="fill-height align-center justify-center"> </v-row>
            </v-carousel-item>
          </v-carousel>
        </v-container>
      </v-main>
    </v-app>
    <div>
      <!-- 使用 ProductShopView 組件 -->
      <ProductShopView></ProductShopView>
    </div>
    <FooterView></FooterView>
  </div>
</template>

<script setup>
import FooterView from "@/views/FooterView.vue";
import HeadUnlogin from "@/views/HeadUnlogin.vue";
import ProductShopView from "./product/ProductShopView.vue";
import { ref } from "vue";

// 輪播圖片數據
const active = ref(0);
const imgs = ref([
  {
    title: "圖片 1",
    image:
      "https://down-ws-tw.img.susercontent.com/tw-11134210-7ras9-m4sdysbtiz7x5c.webp",
  },
  {
    title: "圖片 2",
    image:
      "https://down-ws-tw.img.susercontent.com/tw-11134210-7rask-m4sdysbtkdsd28.webp",
  },
]);
const addToCart = (item) => {
  alert(`${item.name} 已加入購物車！`);
};
</script>

<style>
/* 可選：自定義卡片樣式 */
.v-card {
  border: 2px solid black;
  border-radius: 10px;
  overflow: hidden;
}
</style>
