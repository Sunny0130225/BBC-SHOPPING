<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import stepCheckout from "./stepcheckout/stepCheckout.vue";
import router from "@/router";
import HeadLogin from "@/views/HeadLogin.vue";
import FooterView from "@/views/FooterView.vue";
import Swal from "sweetalert2";
import axios from "axios";

const route = useRoute();
const cartItems = ref([]);
//你ㄉ折扣金額
const DiscountAmount = ref(0);
const selectedDiscount = ref(null);
const selectedDiscountName = ref("");
const memberId = sessionStorage.getItem("memberId");

// 在頁面掛載時接收購物車資料
onMounted(() => {
  const storedCartData = localStorage.getItem("cartData");
  if (storedCartData) {
    cartItems.value = JSON.parse(storedCartData); // 從 LocalStorage 獲取資料
    console.log("接收到的購物車資料：", cartItems.value);
  } else {
    console.warn("購物車資料丟失");
  }
});

// 計算總金額
const totalAmount = computed(() =>
  cartItems.value.reduce(
    (total, item) => total + item.quantity * item.totalPrice,
    0
  )
);

// 刪除項目
const handleDeleteItem = async (item) => {
  try {
    await axios.delete(
      `http://localhost:8081/cartItem/delete/${item.cartItemId}`
    );
    Swal.fire("成功", "商品已刪除", "success");
    fetchCartItems();
  } catch (error) {
    Swal.fire("錯誤", "刪除失敗", "error");
  }
};

const checkout2 = () => {
  if (cartItems.value.length === 0) {
    Swal.fire("提示", "購物車為空，無法進行結帳！", "warning");
  } else {
    localStorage.setItem("cartData", JSON.stringify(cartItems.value)); // 儲存到 LocalStorage
    router.push({ name: "deliveryInfo" });
  }
};

// 選擇折扣券
const seleteDiscount = async () => {
  if (!memberId) {
    Swal.fire("錯誤", "未登入，請先登入", "error");
    return;
  }

  try {
    // 查詢會員的折扣券
    const response = await axios.get(`/api/cart/discounts/${memberId}`);
    const membershipDiscounts = response.data;

    if (!membershipDiscounts.length) {
      Swal.fire("提示", "你沒有可用的折扣券", "warning");
      return;
    }

    // 先檢查 API 回傳的數據
    console.log("獲取的折扣券資料：", membershipDiscounts);

    // 修正資料格式（考慮大小寫問題）
    const options = membershipDiscounts.map((discount) => {
      return {
        value: discount.Did || discount.did, // 兼容大小寫
        text:
          `${discount.Dname || discount.dname || "未知"} - ` +
          `${
            discount.Dtype === "現金" || discount.dtype === "現金"
              ? `NT$${discount.Dpercent || discount.dpercent}`
              : `${discount.Dpercent || discount.dpercent || 0}%`
          } ` +
          `(${discount.Ddepart || discount.ddepart || "無限制"})`,
      };
    });

    console.log("格式化後的折扣券選項：", options);

    // 顯示 SweetAlert2 選擇折扣券
    const { value: selectedDid } = await Swal.fire({
      title: "選擇折扣券",
      input: "select",
      inputOptions: Object.fromEntries(options.map((d) => [d.value, d.text])),
      inputPlaceholder: "請選擇折扣券",
      showCancelButton: true,
    });

    if (!selectedDid) return;

    // 取得所選折扣券的資訊
    const selected = membershipDiscounts.find(
      (d) => (d.Did || d.did) == selectedDid
    );
    selectedDiscount.value = selected;
    selectedDiscountName.value = selected?.Dname || selected?.dname || "未知";

    // 篩選符合條件的購物車商品
    const cartResponse = await axios.get(`/api/cart/items/${memberId}`);
    let filteredCartItems = cartResponse.data;

    if (selected.Ddepart !== "無限制" && selected.ddepart !== "無限制") {
      filteredCartItems = filteredCartItems.filter(
        (item) => item.depart === (selected.Ddepart || selected.ddepart)
      );
    }

    // 計算折扣
    const applicableTotal = filteredCartItems.reduce(
      (sum, item) => sum + item.totalPrice * item.quantity,
      0
    );

    if (selected.Dtype === "現金" || selected.dtype === "現金") {
      DiscountAmount.value = Math.min(
        applicableTotal,
        selected.Dpercent || selected.dpercent
      );
    } else if (selected.Dtype === "百分比" || selected.dtype === "百分比") {
      DiscountAmount.value =
        applicableTotal * ((selected.Dpercent || selected.dpercent) / 100);
    }
    console.log(DiscountAmount, "DiscountAmount");
  } catch (error) {
    console.error("獲取折扣券失敗:", error);

    Swal.fire("錯誤", "無法獲取折扣券", "error");
  }

  // 選擇折扣券後存入 LocalStorage
  localStorage.setItem("DiscountAmount", DiscountAmount.value);
};
</script>

<template>
  <div>
    <HeadLogin></HeadLogin>
  </div>

  <div style="margin: 50px"><stepCheckout></stepCheckout></div>

  <div class="border-cart">
    <div class="bi bi-arrow-right-circle-fill">
      CHECK YOUR ORDER 確認購買明細
    </div>
    <hr style="border: none; border-top: 1px solid black; margin: 10px 0" />

    <div class="checkout-page">
      <table class="table">
        <thead>
          <tr>
            <th>商品名稱</th>
            <th>顏色</th>
            <th>尺寸</th>
            <th>數量</th>
            <th>單價</th>
            <th>小計</th>
            <th>刪除</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cartItems" :key="item.id">
            <td class="product-info">
              <img
                :src="'data:image/jpeg;base64,' + item.picture"
                class="product-image"
              />
              <span class="product-name">{{ item.name }}</span>
            </td>
            <td>{{ item.color }}</td>
            <td>{{ item.size }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.totalPrice }}</td>
            <td>{{ item.quantity * item.totalPrice }}</td>
            <td>
              <button
                @click="handleDeleteItem(item)"
                id="trash-icon"
                class="bi bi-trash3"
              ></button>
            </td>
          </tr>
        </tbody>
      </table>

      <div style="padding: 20px; border-top: 1px dotted black">
        <div style="position: absolute">
          <h6>
            <!-- 你的試算按鈕在這 -->
            <button
              class="btn btn-outline-primary"
              id="checkout-icon"
              @click="seleteDiscount"
            >
              選擇折扣券
            </button>
          </h6>
          <p style="margin-top: 5px; font-size: 14px; color: #555">
            已選折價券：<strong>{{ selectedDiscountName }}</strong>
          </p>
        </div>
        <h6>小計金額：NT$ {{ totalAmount }}</h6>
        <h6>折扣金額：NT$ {{ Math.floor(DiscountAmount) }}</h6>
        <h6 style="font-weight: bold; color: #da0000">
          應付金額：NT$ {{ Math.ceil(totalAmount - DiscountAmount) }}
        </h6>
      </div>
    </div>
  </div>

  <div class="text-center mt-3" style="padding: 2px">
    <router-link to="loginHome">
      <button class="btn btn-outline-secondary" style="margin: 0 5px">
        繼續購物
      </button>
    </router-link>

    <button
      class="btn btn-outline-primary"
      id="checkout-icon"
      @click="checkout2"
    >
      下一步
    </button>
  </div>
  <FooterView></FooterView>
</template>

<style scoped>
.header {
  margin-bottom: 25px;
}

/* 整個購物車框框 */
.border-cart {
  border: 1px solid #e4e4e4;
  border-radius: 10px;
  padding: 25px;
  width: 85%;
  margin: 0 auto;
}

/* 這行字 -> SHOPPING BAG 購物袋 */
.cart {
  color: #666666;
  font-size: 16px;
}

.no-data {
  margin: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
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

.product-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

/* 圖片 */
.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 5px;
}

#trash-icon {
  color: #666666;
}
#trash-icon:hover {
  color: rgb(255, 130, 130);
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
