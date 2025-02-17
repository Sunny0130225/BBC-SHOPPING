import { createRouter, createWebHistory } from "vue-router";
import MemberDetails from "@/components/MemberDetails.vue";
import App from "@/App.vue";
import ProductView from "@/components/ProductView.vue";
import MemberView from "@/components/MemberView.vue";
import IndexComponent from "@/components/IndexComponent.vue";
import LoginCompo from "@/components/LoginCompo.vue";

import morePower from "@/components/morePower.vue";
import OrdersView from "@/components/order/OrdersView.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import ForgetPassword from "@/components/ForgetPassword.vue";

const routes = [
  { path: "/", name: "login", component: LoginCompo },
  {
    path: "/reset-password",
    name: "ResetPassword",
    component: ResetPassword,
  },
  {
    path: "/forget-password",
    name: "ForgetPassword",
    component: ForgetPassword,
  },
  {
    path: "/index", // 父路徑
    name: "index",
    component: IndexComponent, // 父組件
    children: [
      {
        path: "memberView",
        name: "memberView",
        component: MemberView, // 子組件：會員管理
      },
      {
        path: "productView",
        name: "productView",
        component: ProductView, // 子組件：商品管理
      },
      {
        path: "ordersView",
        name: "ordersView",
        component: OrdersView, // 子組件：商品管理
      },
      {
        path: "/detail/:id", // 動態參數路徑
        name: "MemberDetail", // 路由名稱
        component: MemberDetails, // 對應組件
      },
      {
        path: "/morePower",
        name: "morePower", // 這裡的 name 要跟 this.$router.push 一致
        component: morePower,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(), // 使用 HTML5 模式
  routes,
});

// 導航守衛 透過router間的跳轉去檢查有無登入
router.beforeEach((to, from, next) => {
  const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true"; // 判斷是否已登入

  if (
    to.name === "login" ||
    to.name === "ResetPassword" ||
    to.name === "ForgetPassword"
  ) {
    // Login頁直接允許導航
    next();
  } else if (!isLoggedIn) {
    // 如果未登入且嘗試訪問受保護的頁面，跳轉到 Login
    next({ name: "login" });
  } else {
    // 已登入，允許導航到受保護的頁面
    next();
  }
});

export default router;
