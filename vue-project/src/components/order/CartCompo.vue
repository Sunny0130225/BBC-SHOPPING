<script setup>
import FooterView from '@/views/FooterView.vue';
import HeadLogin from '@/views/HeadLogin.vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

// 定義購物車商品資料
const cartItems = ref([]);

// 查詢購物車項目
const fetchCartItems = async () => {
  try {
    const response = await axios.get("http://localhost:8081/cartItem/selectAll");
    console.log(response.data);
    cartItems.value = response.data;
    
  } catch (error) {
    console.error("無法獲取訂單資料:", error);
  }
};

// 初始加載購物車項目
onMounted(fetchCartItems);

//更新商品數量
const updateQuantity = async (item) => {
  try {
    // 使用 URL 查詢參數傳遞 quantity
    await axios.put(`http://localhost:8081/cartItem/update/${item.cartItemId}`, null, {
      params: { quantity: item.quantity },
    });
    // 獲取更新後的購物車資料
    await fetchCartItems();
  } catch (error) {
    console.error("更新商品數量失敗:", error);
  }
};

// 計算總金額
const totalAmount = computed(() =>
  cartItems.value.reduce((total, item) => total + item.quantity * item.totalPrice, 0)
);

// 刪除項目
const handleDeleteItem = async (item) => {
  try {
    await axios.delete(`http://localhost:8081/cartItem/delete/${item.cartItemId}`);
    Swal.fire("成功", "商品已刪除", "success");
    fetchCartItems();
  } catch (error) {
    Swal.fire("錯誤", "刪除失敗", "error");
  }
};

const router = useRouter();
//結帳功能
const checkout = () => {
  if (cartItems.value.length === 0) {
    Swal.fire("提示", "購物車為空，無法進行結帳！", "warning");
  } else {
    localStorage.setItem("cartData", JSON.stringify(cartItems.value)); // 儲存到 LocalStorage
    router.push({ name: "checkout" });
  }
};
</script>

<template>
  <div>
    <HeadLogin></HeadLogin>
  </div>

  <div class="border-cart">
      <div>
        <div class="bi-cart-check"> SHOPPING CART 購物車</div>
      </div>
        <hr style="border: none; border-top: 1px solid black; margin: 10px 0;">

      <!-- 判斷購物車是否有商品 -->
  <div v-if="cartItems.length === 0">
      <div class="no-data">購物車目前是空的!</div>
  </div>
  <div v-else>
      <table class="table">
        <thead>
          <tr>
            <th>商品名稱</th>
            <th>顏色</th>
            <th>尺寸</th>
            <th>數量</th>
            <th>單價</th>
            <th>總計</th>
            <th>刪除</th>
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
            <td>
              <select v-model="item.quantity" @change="updateQuantity(item)" class="form-select">
                <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
              </select>
            </td>
            <td>{{ item.totalPrice }}</td>
            <td>{{ item.quantity * item.totalPrice  }}</td>
            <td>
              <button @click="handleDeleteItem(item)" 
                id="trash-icon" 
                class="bi bi-trash3" 
                style="cursor:pointer;"></button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  
  <!-- 小計 -->
  <div class="text-end">
    <h6 style="padding :20px; border-top: 1px dotted black;">小計金額：NT$ {{ totalAmount }}</h6>
  </div>
  
</div>
    <div class="text-center mt-3" style="padding: 2px;">
      <router-link to="loginHome">
        <button class="btn btn-outline-secondary" 
        style="margin: 0 5px;">繼續購物</button>
      </router-link>

      <button class="btn btn-outline-primary" 
              id="checkout-icon"
              @click="checkout">結帳</button>
</div>
<FooterView></FooterView>
</template>

<style lang="css" scoped>

.header{
  margin-bottom: 25px;
}

/* 整個購物車框框 */
.border-cart{
  border: 1px solid #e4e4e4;
  border-radius: 10px;
  padding: 25px;
  width: 85%; 
  margin: 0 auto;
    
}

/* 這行字 -> SHOPPING BAG 購物袋 */
.cart{
  color: #666666;
  font-size: 16px;
}

.no-data{
  margin: 50px;
  display: flex;
  align-items: center;
  justify-content:center;
}

/* 購物車裡面的東西 */
.table {
  width: 90%;
  border: none;
  margin: 0 auto;
}

.table th, .table td {
  padding: 12px; 
  text-align: center; 
  font-size: 14px; 
  border: none; 
  
}
.table th {
  border-bottom: 1px dotted	 #666666;
  color: #333; 
  font-size: 16px; 
  font-weight: bold; 
  
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

#trash-icon{
  color: #666666;
}
#trash-icon:hover{
  color: rgb(255, 130, 130);
}
#checkout-icon{
  color: #C48888;
  border-color: #C48888;
}
#checkout-icon:hover{
  background-color: #C48888;
  color: white;
}


</style>
