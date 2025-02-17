import DiscountView from "@/components/discount/DiscountView.vue";
import HomeCompo from "@/components/HomeCompo.vue";
import LoginHome from "@/components/LoginHome.vue";
import LoginCompo from "@/components/member/LoginCompo.vue";
import MemberCompo from "@/components/member/MemberCompo.vue";
import RequireLogin from "@/components/member/RequireLogin.vue";
import CartCompo from "@/components/order/CartCompo.vue";
import ReviewCompo from "@/components/review/ReviewCompo.vue";
import { createRouter, createWebHistory } from "vue-router";
import MemberIndex from "@/components/member/MemberIndex.vue";
import MemberOrder from "@/components/order/MemberOrder.vue";
import MemberDiscount from "@/components/discount/MemberDiscount.vue";
import LuckyDraw from "@/components/discount/LuckyDraw.vue";
import ResetPassword from "@/components/member/ResetPassword.vue";
import ForgetPassword from "@/components/member/ForgetPassword.vue";
import ProductDetail from "@/components/product/ProductDetail.vue";
import CheckoutCompo from "@/components/order/CheckoutCompo.vue";
import DeliveryInfo from "@/components/order/DeliveryInfo.vue";
import OrderComplete from "@/components/order/OrderComplete.vue";
import ChangePassword from "@/components/member/ChangePassword.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), //一定要寫這一句創建路由對象才可以用路由
  routes: [
    { path: "/", name: "Home", component: HomeCompo },
    { path: "/loginHome", name: "loginHome", component: LoginHome },
    { path: "/login", name: "login", component: LoginCompo },
    { path: "/member", name: "member", component: MemberCompo },
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
      path: "/member",
      name: "member",
      component: MemberIndex,
      redirect: "/member/memberDetails",
      children: [
        {
          path: "memberDetails",
          component: MemberCompo,
        },
        {
          path: "changePassword",
          component: ChangePassword,
        },
        {
          path: "myOrder",
          component: MemberOrder,
        },
        {
          path: "myDiscount",
          component: MemberDiscount,
        },
      ],
    },
    { path: "/cart", name: "cart", component: CartCompo },
    { path: "/checkout", name: "checkout", component: CheckoutCompo },
    { path: "/deliveryInfo", name: "deliveryInfo", component: DeliveryInfo },
    { path: "/orderComplete", name: "orderComplete", component: OrderComplete },
    { path: "/memberOrder", name: "memberOrder", component: MemberOrder },
    { path: "/require", name: "require", component: RequireLogin },
    { path: "/review", name: "review", component: ReviewCompo },
    { path: "/discount", name: "discount", component: DiscountView },
    { path: "/luckydraw", name: "luckydraw", component: LuckyDraw },
    {
      path: "/product/:id", // 商品詳情頁
      name: "ProductDetail",
      component: ProductDetail,
      props: true, // 將路由參數作為 props 傳遞
    },
  ],
});

// 導航守衛 透過router間的跳轉去檢查有無登入
router.beforeEach((to, from, next) => {
  const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true"; // 判斷是否已登入

  if (
    to.name === "login" ||
    to.name === "Home" ||
    to.name === "ResetPassword" ||
    to.name === "ForgetPassword" ||
    to.name === "ProductDetail"
  ) {
    // Login 和 Home 頁直接允許導航
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
