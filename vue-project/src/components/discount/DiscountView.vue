<template>
<div class="coupon-container">
  <h2>購物車</h2>
  <table class="coupon-table">
    <thead>
      <tr>
        <th>產品ID</th>
        <th>部門</th>
        <th>數量</th>
        <th>總價</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in cart" :key="item.productId">
        <td>{{ item.productId }}</td>
        <td>{{ item.depart }}</td>
        <td>{{ item.quantity }}</td>
        <td>
          <span v-if="item.discountedTotal !== undefined">
            <del>{{ item.totalPrice.toFixed(0) }}元</del>  
            {{ item.discountedTotal.toFixed(0) }}元
          </span>
          <span v-else>{{ item.totalPrice.toFixed(0) }}</span>
        </td>
      </tr>
    </tbody>
  </table>

  <!-- 顯示購物車總金額 -->
  <div class="total-amount">
    <h3>購物車總金額: {{ DiscountAmount.toFixed(0) }}</h3>
  </div>

  <button @click="showDiscounts = !showDiscounts">使用折價券</button>

  <!-- 折價券彈窗 -->
  <div v-if="showDiscounts" class="modalcoupon-container">
    <h3>可用折價券</h3>
    <div v-for="discount in discounts" :key="discount.Did">
      <button @click="applyDiscount(discount.Did)">
        {{ discount.Dname }} (剩餘次數: {{ discount.remaining }})
      </button>
    </div>
  </div>
</div>
</template>

<script>
import axios from 'axios';

export default {
data() {
  return {
    memberId: null,
    cart: [],
    discounts: [],
    showDiscounts: false,
    totalAmount: 0,
    DiscountAmount: 0       // 折扣後總金額
  };
},
created() {
  const storedMemberId = sessionStorage.getItem("memberId");
  if (storedMemberId) {
    this.memberId = parseInt(storedMemberId, 10);
    this.fetchCart();
    this.fetchDiscounts();
  } else {
    console.warn("會員ID不存在於 sessionStorage");
  }
},
methods: {
  fetchCart() {
    if (this.memberId) {
      axios.get(`/cart/${this.memberId}`).then(response => {
        this.cart = response.data;
        this.calculateTotal();
      }).catch(error => {
        console.error("獲取購物車失敗:", error);
      });
    }
  },
  fetchDiscounts() {
    if (this.memberId) {
      axios.get(`/cart/${this.memberId}/discounts`).then(response => {
        this.discounts = response.data;
      }).catch(error => {
        console.error("獲取折扣券失敗:", error);
      });
    }
  },
  applyDiscount(discountId) {
if (this.memberId) {
  axios.post(`/cart/${this.memberId}/apply-discount/${discountId}`)
    .then(response => {
      const discountedItems = response.data;

      // 套用新折扣前，先重置所有商品的折扣價格
      this.cart = this.cart.map(item => ({
        ...item,
        discountedTotal: undefined  // 重置折扣價格，回到原價
      }));

      // 更新購物車中的每個項目，套用新折扣
      this.cart = this.cart.map(item => {
        const discountedItem = discountedItems.find(d => d.productId === item.productId);
        if (discountedItem) {
          return {
            ...item,
            discountedTotal: discountedItem.discountedTotal,
            originalTotal: item.totalPrice  // 保留原始價格
          };
        }
        return item;
      });

      // 計算新的總金額
      this.calculateTotal();

      // 關閉折價券視窗
      this.showDiscounts = false;
    })
    .catch(error => {
      alert(error.response.data);
    });
}
}
,

calculateTotal() {
// 計算原價總金額
const originalTotal = this.cart.reduce((sum, item) => sum + item.totalPrice, 0);
this.totalAmount = originalTotal;

// 計算折扣後的總金額
const discountedTotal = this.cart.reduce((sum, item) => {
  return sum + (item.discountedTotal !== undefined ? item.discountedTotal : item.totalPrice);
}, 0);
this.DiscountAmount = discountedTotal;
}

}
};
</script>



<style lang="css" scoped></style>
<style>



/* 主容器樣式 */
.coupon-container {
max-width: 800px;
margin: 20px auto;
text-align: center;
font-family: Arial, sans-serif;
}

/* 標題樣式 */
.coupon-container h1 {
font-size: 28px;
color: #333;
margin-bottom: 20px;
}

/* 表格樣式 */
.coupon-table {
width: 100%;
border-collapse: collapse;
margin: 0 auto;
font-size: 16px;
text-align: left;
box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.coupon-table thead th {
background-color: #c48888;
color: black;
padding: 10px;
border-bottom: 2px solid #ddd;
}

.coupon-table tbody td {
padding: 10px;
border-bottom: 1px solid #ddd;
}

.coupon-table tbody tr:hover {
background-color: #f9f9f9;
}

/* 表格行 alternation */
.coupon-table tbody tr:nth-child(even) {
background-color: #f5f5f5;
}

.coupon-table tbody tr:nth-child(odd) {
background-color: #ffffff;
}

/* 到期日顯示紅色 */
.coupon-table tbody td:last-child {
color: #e74c3c; /* 紅色 */
font-weight: bold;
}

/* 手機響應式設計 */
@media (max-width: 600px) {
.coupon-table {
  font-size: 14px;
}

.coupon-table thead {
  display: none; /* 隱藏表頭 */
}

.coupon-table tbody tr {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  border-bottom: 2px solid #ddd;
}

.coupon-table tbody td {
  display: flex;
  justify-content: space-between;
  padding: 8px;
}

.coupon-table tbody td::before {
  content: attr(data-label);
  font-weight: bold;
  margin-right: 10px;
}
}


</style>