<script setup>
import FooterView from "@/views/FooterView.vue";
import HeadLogin from "@/views/HeadLogin.vue";
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import step2Checkout from "./stepcheckout/step2Checkout.vue";
import Swal from "sweetalert2";
import axios from "axios";

const router = useRouter();
const cartItems = ref([]);
//折扣金額
const DiscountAmount = ref(0);
// 收件人資訊
const recipientName = ref("");
const phoneNumber = ref("");
const pickupStore = ref("民生店");
const paymentMethod = ref("信用卡");

// 接收傳遞的購物車數據
onMounted(() => {
  const storedCartData = localStorage.getItem("cartData");
  if (storedCartData) {
    cartItems.value = JSON.parse(storedCartData); // 從 LocalStorage 獲取資料
    console.log("接收到的購物車資料：", cartItems.value);
  } else {
    console.warn("購物車資料丟失");
  }

  // 從 LocalStorage 取得折扣金額
  const storedDiscount = localStorage.getItem("DiscountAmount");
  DiscountAmount.value = storedDiscount ? Math.floor(Number(storedDiscount)) : 0;
  console.log("折扣金額：", DiscountAmount.value);
});

// 計算總金額
const totalAmount = computed(() =>
  cartItems.value.reduce(
    (total, item) => total + item.quantity * item.totalPrice,
    0
  )
);

// 確定結帳：發送資料到後端
const checkout3 = async () => {
  // 取得 localStorage 購物車資料，解析JSON字串
  const storedCartData = localStorage.getItem("cartData");
  const cartItems = JSON.parse(storedCartData);
  console.log(cartItems);

  // 取得會員 ID
  const memberId = sessionStorage.getItem("memberId");
  console.log("memberId:", memberId);

  // 建立訂單資料
  const orderData = {
    memberId: sessionStorage.getItem("memberId"),
    order: {
      orderDate: new Date().toISOString().split("T")[0],
      paymentMethod: paymentMethod.value,
      pickupStore: pickupStore.value,
      status: "待付款", 
      totalAmount: Math.floor(totalAmount.value - DiscountAmount.value) // 確保傳遞應付金額

    },
  };

  try {
    // 先發送 `Orders`，取得 `orderId`
    const orderResponse = await axios.post(
      "http://localhost:8081/orders/checkout",
      orderData
    );
    if (orderResponse.status !== 201) {
      throw new Error("訂單建立失敗");
    }

    const orderId = orderResponse.data.orderId; // 確保後端回傳 `orderId`
    console.log("新建訂單成功，OrderID:", orderId);

    // 逐筆新增 `OrderItem`
    for (const item of cartItems) {
      const orderItemData = {
        memberId: memberId,
        orderId: orderId, // **關聯訂單 ID**
        productId: item.productId,
        quantity: item.quantity,
        size: item.size,
        color: item.color,
        unitPrice: item.totalPrice,
        subTotal: item.quantity * item.totalPrice,
        discountAmount: DiscountAmount.value,
      };
      console.log("新增 OrderItem 成功:", orderItemData);

      const itemResponse = await axios.post(
        "http://localhost:8081/orderItems/checkoutItem",
        orderItemData
      );
      if (itemResponse.status !== 201) {
        throw new Error("商品加入訂單失敗");
      }
    }
    
    //清除購物車，跳轉到成功頁面
    localStorage.removeItem("cartData");
    Swal.fire({
      title: "結帳成功",
      text: "您的訂單已成功建立！",
      icon: "success",
      confirmButtonText: "確定",
    }).then(() => {
      router.push("/orderComplete");
    });



  } catch (error) {
    console.error("結帳請求失敗：", error);
    Swal.fire({
      title: "結帳失敗",
      text: error.response?.data || "系統異常，請稍後再試！",
      icon: "error",
      confirmButtonText: "確定",
    });
  }
};
</script>

<template>
  <div>
    <div>
      <HeadLogin></HeadLogin>
    </div>

    <div style="margin: 50px"><step2Checkout></step2Checkout></div>

    <!-- 確認購物明細 -->
    <div class="border-cart">
      <div class="bi bi-arrow-right-circle-fill">
        CHECK YOUR ORDER 確認購買明細
      </div>
      <hr style="border: none; border-top: 1px solid black; margin: 10px 0" />

      <table class="table">
        <thead>
          <tr>
            <th>商品名稱</th>
            <th>顏色</th>
            <th>尺寸</th>
            <th>數量</th>
            <th>單價</th>
            <th>小計</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cartItems" :key="item.id">
            <td class="product-info">
              <img :src="'data:image/jpeg;base64,' + item.picture" class="product-image"/>
              <span class="product-name">{{ item.name }}</span>
            </td>
            <td>{{ item.color }}</td>
            <td>{{ item.size }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.totalPrice }}</td>
            <td>{{ item.quantity * item.totalPrice }}</td>
          </tr>
        </tbody>
      </table>

      <div style="padding: 20px; border-top: 1px dotted black">
        <h6>小計金額：NT$ {{ totalAmount }}</h6>
        <h6>折扣金額：NT$ {{ Math.floor(DiscountAmount) }} </h6>
        <h6 style="font-weight: bold; color: #da0000">
          應付金額：NT$ {{ Math.floor(totalAmount - DiscountAmount) }}
        </h6>
      </div>
    </div>

    <br />
    <br />

    <!-- 配送與付款方式 -->
    <div class="shipping-payment">
      <div class="bi bi-arrow-right-circle-fill">
        SHIPPING & PAYMENT 配送與付款方式
      </div>
      <hr style="border: none; border-top: 1px solid black; margin: 10px 0" />

      <div class="shipping-options" style="padding: 20px">
        <label for="creditCard">
          <input
            type="radio"
            id="creditCard"
            value="信用卡"
            v-model="paymentMethod"
            checked
          />
          信用卡線上刷卡
        </label>
        <br />
        <br />
        <label for="Pickup711">
          <input
            type="radio"
            id="Pickup711"
            value="現金"
            v-model="paymentMethod"
          />
          超商取貨付款
        </label>
      </div>
    </div>

    <br />
    <br />

    <!-- 填寫收件資料 -->
    <div class="delivery-Information">
      <div class="bi bi-arrow-right-circle-fill">
        DELIVERY INFORMATION 填寫收件資料
      </div>
      <hr style="border: none; border-top: 1px solid black; margin: 10px 0" />

      <div class="delivery-options" style="padding: 20px">
        <div class="form-group">
          <label for="recipientName">收件人姓名：</label>
          <input
            value="recipientName"
            v-model="recipientName"
            type="text"
            class="info"
          />
        </div>
        <br />
        <div class="form-group">
          <label for="phoneNumber">手機號碼：</label>
          <input
            value="phoneNumber"
            v-model="phoneNumber"
            type="tel"
            class="info"
          />
        </div>
        <br />
        <div class="form-group">
          <label for="pickupStore">選擇取貨店鋪：</label>
          <select v-model="pickupStore" class="info">
            <option value="民生店">民生店</option>
            <option value="聖文店">聖文店</option>
            <option value="仁化店">仁化店</option>
            <option value="民智店">民智店</option>
            <option value="懷智店">懷智店</option>
            <option value="仁德店">仁德店</option>
          </select>
        </div>
      </div>
    </div>

    <br />
    <div class="text-center mt-3" style="padding: 2px">
      <button
        class="btn btn-outline-primary"
        id="checkout-icon"
        @click="checkout3"
      >
        確定結帳
      </button>
    </div>

    <FooterView></FooterView>
  </div>
</template>

<style scoped>
/* 整個購物車框框 */
.border-cart,
.shipping-payment,
.delivery-Information {
  border: 1px solid #e4e4e4;
  border-radius: 10px;
  padding: 25px;
  width: 85%;
  margin: 0 auto;
}

/* 購物車裡面的東西 */
.table {
  width: 90%;
  border: none;
  margin: 0 auto;
}
.table th,
.table td {
  padding: 12px;
  text-align: center;
  font-size: 14px;
  border: none;
}
.table th {
  border-bottom: 1px dotted #666666;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}

/* 總金額 */
h6 {
  display: flex;
  justify-content: flex-end;
}

/* 收件資料 */
.info {
  border: 1px solid #e4e4e4; /* 灰色邊框 */
  padding: 8px;
  margin-bottom: 10px;
  border-radius: 4px;
}

/* 圖片 */
.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 5px;
}

.product-info {
  display: flex;
  align-items: center; 
  justify-content: center;
  gap: 20px; 
}

#checkout-icon {
  color: #c48888;
  border-color: #c48888;
}
#checkout-icon:hover {
  background-color: #c48888;
  color: white;
}
</style>
