import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";

// https://vite.dev/config/
export default defineConfig({
  base: "/customer/", // 應用的基礎路徑
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  // 跨域 前端需要做連到後端
  server: {
    proxy: {
      "/spring": {
        target: "http://localhost:8081", // 後端地址
        changeOrigin: true, // 修改請求的來源，避免跨域問題
        rewrite: (path) => path.replace(/^\/spring/, ""), // 移除 `/spring` 前綴
      },
    },
  },
});
