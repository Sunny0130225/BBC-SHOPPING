import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import axios from "axios";

// 引入 Bootstrap CSS 和 JS、icons
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap-icons/font/bootstrap-icons.css";

axios.defaults.baseURL = "http://localhost:8081";

import App from "./App.vue";
import router from "./router";

const app = createApp(App);

app.use(createPinia());
app.use(router);

// 引入 Vuetify
import { createVuetify } from "vuetify";
import "vuetify/styles"; // 引入樣式
import "@mdi/font/css/materialdesignicons.css"; // 引入圖標
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
// 建立 Vuetify 實例
const vuetify = createVuetify({
  components,
  directives,
});

// 使用 Vuetify
app.use(vuetify);

app.mount("#app");


import 'bootstrap/dist/js/bootstrap.bundle.min';
