<template>
  <div>
    <div class="border-cart">
      <div class="bi bi-arrow-right-circle-fill">訂單查詢</div>
      <hr style="border: none; border-top: 1px solid black; margin: 10px 0" />

      <br />

      <div class="seleteBtn">
        <!-- 訂單狀態按鈕 -->
        <button
          v-for="statues in status"
          :key="statues.value"
          @click="fetchOrdersByStatus(statues.value)"
          class="btn"
          :class="{ active: selectedStatus === statues.value }"
        >
          {{ statues.name }}
        </button>
      </div>

      <div id="ordersTable">
        <!-- 訂單資料表格 -->
        <table>
          <thead>
            <tr>
              <th v-for="column in head" :key="column.value">
                {{ column.title }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in ordersWithTotal" :key="order.orderId">
              <td>{{ order.orderId }}</td>
              <td>{{ order.orderDate }}</td>
              <td>{{ order.totalDiscount }}</td>
              <td>{{ order.totalAmount }}</td>
              <td>{{ order.paymentMethod }}</td>
              <td>{{ order.finalAmount }}</td>
              <td>{{ order.pickupStore }}</td>
              <td>{{ order.pickupDate }}</td>
              <td>{{ order.status }}</td>
              <td>
                <div class="text-center mt-3" style="padding: 10px">
                  <button
                    @click="orderItemList(order.member.id, order.orderId)"
                    class="btn btn-outline-primary"
                    style="margin: 0 5px"
                    id="checkout-icon"
                  >
                    檢視詳情
                  </button>

                  <button
                    @click="cancelOrder(order)"
                    class="btn btn-outline-secondary"
                  >
                    取消訂單
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 訂單詳情表格 -->
    <div v-if="showDialog" class="overlay">
      <div class="dialog">
        <div class="header">
          <h4 style="font-weight: bolder">訂單詳情</h4>
          <button
            @click="closeDialog"
            class="bi-x-circle"
            style="font-size: 25px; margin: 10px"
          ></button>
        </div>

        <table>
          <thead>
            <tr>
              <th>商品名稱</th>
              <th></th>
              <th>尺寸</th>
              <th>顏色</th>
              <th>數量</th>
              <th>單價</th>
              <th>折扣金額</th>
              <th>總金額</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in selectedOrderItems" :key="item.id">
              <td><img :src="'data:image/jpeg;base64,' + item.productBean.picture" 
                class="product-image"/>
            </td>
              <td>{{ item.productBean.name }}</td>
              <td>
                {{ item.productBean.productDetails?.[0]?.size || "無尺寸" }}
              </td>
              <td>
                {{ item.productBean.productDetails?.[0]?.color || "無顏色" }}
              </td>
              <td>{{ item.quantity }}</td>
              <td>{{ item.unitPrice }}</td>
              <td>{{ item.discountAmount }}</td>
              <td>
                {{ item.quantity * item.unitPrice - item.discountAmount }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { ref, computed, onMounted } from "vue";

const status = ref([
  { name: "全部", value: "全部" },
  { name: "已付款", value: "已付款" },
  { name: "待出貨", value: "待出貨" },
  { name: "待付款", value: "待付款" },
  { name: "已完成", value: "已完成" },
  { name: "退貨/退款", value: "退貨/退款" },
  { name: "取消", value: "取消" },
]);

const head = ref([
  { title: "訂單編號", value: "orderId" },
  { title: "訂單日期", value: "orderDate" },
  { title: "折扣金額", value: "discountAmount" },
  { title: "總金額", value: "totalAmount" },
  { title: "付款方式", value: "paymentMethod" },
  { title: "最終金額", value: "finalAmount" },
  { title: "取貨店鋪", value: "pickupStore" },
  { title: "取貨日期", value: "pickupDate" },
  { title: "訂單狀態", value: "status" },
  { title: "操作", value: "actions" },
]);

const orders = ref([]);
const selectedStatus = ref("全部"); // 追蹤目前選擇的狀態
const memberId = ref(sessionStorage.getItem("memberId")); // 假設要篩選的會員 ID

// 計算訂單金額
const ordersWithTotal = computed(() => {
  return filteredOrders.value.map((order) => {
    const totalAmount = order.orderItems.reduce(
      (sum, item) => sum + item.quantity * item.unitPrice,
      0
    );
    const totalDiscount = order.orderItems.reduce(
      (sum, item) => sum + item.discountAmount,
      0
    );
    const finalAmount = totalAmount - totalDiscount;

    return {
      ...order,
      totalAmount,
      totalDiscount,
      finalAmount,
    };
  });
});

// 計算篩選後的訂單
const filteredOrders = computed(() => {
  if (selectedStatus.value === "全部") {
    return orders.value;
  }
  return orders.value.filter((order) => order.status === selectedStatus.value);
});

// 取得會員所有訂單
const fetchOrders = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8081/orders/member/${memberId.value}`
    );
    orders.value = response.data;
  } catch (error) {
    console.error("無法獲取訂單資料:", error);
  }
};

// 依據選擇的狀態獲取會員訂單
const fetchOrdersByStatus = async (status) => {
  selectedStatus.value = status;
  if (status === "全部") {
    await fetchOrders();
  } else {
    try {
      const response = await axios.get(
        `http://localhost:8081/orders/member/${memberId.value}/status/${status}`
      );
      orders.value = response.data;
    } catch (error) {
      console.error("無法獲取特定狀態的訂單:", error);
    }
  }
};

// 頁面載入時先獲取所有訂單
onMounted(() => {
  fetchOrders();
});

const showDialog = ref(false);
const selectedOrderItems = ref([]);
const selectedMemberId = ref(null);
const selectedOrderId = ref(null);

//訂單項目表格
const orderItemList = async (memberId, orderId) => {
  selectedMemberId.value = memberId;
  selectedOrderId.value = orderId;
  try {
    const response = await axios.get(
      `http://localhost:8081/orderItems/selectByMemberId/${memberId}`
    );
    selectedOrderItems.value = response.data;
    showDialog.value = true;
    console.log("獲取的訂單資料", selectedOrderItems.value);
  } catch (error) {
    console.error("無法獲取訂單項目資料:", error);
  }
};

//關閉清單
const closeDialog = () => {
  showDialog.value = false;
};

//取消訂單
const cancelOrder = async (order) => {
  const result = await Swal.fire({
    title: "確定取消訂單嗎?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "確定",
    cancelButtonText: "取消",
  });

  if (result.isConfirmed) {
    const response = await axios.post("http://localhost:8081/orders/update", {
      orderId: order.orderId,
      status: "取消",
    });
    if (response.status === 200) {
      order.status = "取消";
      Swal.fire("已取消!", "訂單已成功取消。", "success");
    } else {
      Swal.fire("錯誤", "取消訂單時發生錯誤!", "error");
    }
  }
};
</script>

<style scoped>
.border-cart {
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 30px;
  width: 90%;
  margin: auto;
  background: white;
}

.seleteBtn {
  display: flex;
  justify-content: center;
  gap: 10px;
}

/* 圖片 */
.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 5px;
}

button.active {
  background-color: #c48888;
  color: white;
}

#ordersTable {
  width: 90%;
  /* margin: 20px auto; */
  padding: 40px;
  border-collapse: collapse;
  display: flex;
  justify-content: center;
  align-items: center;
}

th,
td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #ddd;
  border-left: none;
  border-right: none;
}

th {
  background-color: #f4f4f4;
  font-weight: bold;
}

#checkout-icon {
  color: #c48888;
  border-color: #c48888;
}
#checkout-icon:hover {
  background-color: #c48888;
  color: white;
}

.dialog {
  justify-content: center;
  padding: 20px;
  background: #ececec;
  border: 10px solid #ececec;
  border-radius: 10px;
  /* width: 70%; */
}

.dialodIndiv {
  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.bi-x-circle:hover {
  color: #ececec;
}
</style>
