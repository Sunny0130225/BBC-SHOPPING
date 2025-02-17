import { createApp } from "vue";
import { createPinia } from "pinia";
import "@/assets/style.css";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";
import "@mdi/font/css/materialdesignicons.css";
// 引入 Bootstrap 的 CSS 和 JS
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";

const app = createApp(App);


app.use(createPinia());
app.use(router);
app.use(vuetify);
app.mount("#app");
