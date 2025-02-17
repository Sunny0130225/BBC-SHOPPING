<template>
  <div>
    <!-- 訂單資料表格 -->
    <table id="ordersTable" style="width: 85%">
      <thead>
        <tr>
          <th v-for="column in head" :key="column.value">{{ column.title }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in ordersWithTotal" :key="order.orderId">
          <!-- 顯示訂單資訊 -->
          <td>{{ order.orderId }}</td>
          <td>{{ order.member.id }}</td>
          <td>{{ order.member.username }}</td>
          <td>{{ order.orderDate }}</td>
          <td>{{ order.totalDiscount }}</td>
          <td>{{ order.totalAmount }}</td>
          <td>{{ order.paymentMethod }}</td>
          <td>{{ order.finalAmount }}</td>
          <td>{{ order.pickupStore }}</td>
          <td>{{ order.pickupDate }}</td>
          <td>{{ order.status }}</td>
          <td>
            <!-- 操作區域 -->
            <button
              @click="orderItemList(order.member.id, order.orderId)"
              class="bi bi-list-check"
            ></button>
            <button @click="editOrder(order)" class="bi bi-pencil"></button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 顯示選擇的訂單項目 -->
    <OrderItemDialog
      v-if="showDialog"
      :orderItems="selectedOrderItems"
      :memberId="selectedMemberId"
      :orderId="selectedOrderId"
      @closeDialog="closeDialog"
      @updateItem="handleUpdateItem"
      @insertItem="handleInsertItem"
      @deleteItem="handleDeleteItem"
    />
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted, computed } from "vue";
import Swal from "sweetalert2";
import $ from "jquery";
import "datatables.net";
import "datatables.net-dt";
import OrderItemDialog from "./OrderItemDialog.vue";

const head = ref([
  { title: "訂單編號", value: "orderId" },
  { title: "會員ID", value: "memberId" },
  { title: "會員名稱", value: "userName" },
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

const orders = ref([]); // 用來存儲訂單資料
const selectedMemberId = ref(null); // 儲存當前選擇的會員ID
const selectedOrderId = ref(null); // 儲存當前選擇的訂單ID
const selectedOrderItems = ref([]); //儲存清單細項資料
const showDialog = ref(false);

// 計算訂單的總金額
const ordersWithTotal = computed(() => {
  return orders.value.map((order) => {
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

// 初始化時取得訂單資料
onMounted(async () => {
  await fetchOrders();
  // 確保表格已渲染完成後再初始化 DataTable
  $("#ordersTable").DataTable({
    searching: false, // 禁用搜尋框
    paging: false, // 禁用分頁
  });
});

console.log($.fn.dataTable);

// 定義用來獲取訂單資料的函數
const fetchOrders = async () => {
  try {
    const response = await axios.get("http://localhost:8081/orders/selectAll");
    console.log(response.data);
    orders.value = response.data; // 更新 orders 資料
  } catch (error) {
    console.error("無法獲取訂單資料:", error);
  }
};

// 顯示選擇的會員訂單項目
const orderItemList = async (memberId, orderId) => {
  selectedMemberId.value = memberId; // 更新選擇的會員ID
  selectedOrderId.value = orderId; // 確保選中的 orderId 被設置

  try {
    const response = await axios.get(
      `http://localhost:8081/orderItems/selectByMemberId/${memberId}`
    );
    selectedOrderItems.value = response.data; // 更新選擇的訂單項目
    showDialog.value = true; // 顯示對話框
  } catch (error) {
    console.error("無法獲取訂單項目資料:", error);
  }
};

// 關閉對話框
const closeDialog = () => {
  showDialog.value = false;
};

// 編輯訂單
const editOrder = async (order) => {
  const { value: formValues } = await Swal.fire({
    title: "修改訂單",
    html: `
          <div>
            <div>
              <label for="orderId">訂單編號:</label>
              <input style="cursor: not-allowed; background-color: #f0f0f0;" id="orderId" class="swal2-input" value="${
                order.orderId
              }" readonly>
            </div>
            <div>
              <label for="orderDate">訂單日期:</label>
              <input type="date" id="orderDate" class="swal2-input" value="${
                order.orderDate
              }">
            </div>
            <div>
              <label for="pickupStore">取貨店鋪:</label>
              <select id="pickupStore" class="swal2-input">
                <option value="民生店" ${
                  order.pickupStore === "民生店" ? "selected" : ""
                }>民生店</option>
                <option value="聖文店" ${
                  order.pickupStore === "聖文店" ? "selected" : ""
                }>聖文店</option>
                <option value="仁化店" ${
                  order.pickupStore === "仁化店" ? "selected" : ""
                }>仁化店</option>
                <option value="民智店" ${
                  order.pickupStore === "民智店" ? "selected" : ""
                }>民智店</option>
                <option value="懷智店" ${
                  order.pickupStore === "懷智店" ? "selected" : ""
                }>懷智店</option>
                <option value="仁德店" ${
                  order.pickupStore === "仁德店" ? "selected" : ""
                }>仁德店</option>
              </select>
            </div>

            <div>
              <label for="paymentMethod">付款方式:</label>
              <select id="paymentMethod" class="swal2-input">
                <option value="信用卡" ${
                  order.paymentMethod === "信用卡" ? "selected" : ""
                }>信用卡</option>
                <option value="現金付款" ${
                  order.paymentMethod === "現金付款" ? "selected" : ""
                }>現金付款</option>
              </select>
            </div>
            <div>
              <label for="pickupDate">取貨日期:</label>
              <input type="date" id="pickupDate" class="swal2-input" value="${
                order.pickupDate
              }">
            </div>
            <div>
              <label for="status">訂單狀態:</label>
              <select id="status" class="swal2-input">
                <option value="待付款" ${
                  order.status === "待付款" ? "selected" : ""
                }>待付款</option>
                <option value="待出貨" ${
                  order.status === "待出貨" ? "selected" : ""
                }>待出貨</option>
                <option value="待收貨" ${
                  order.status === "待收貨" ? "selected" : ""
                }>待收貨</option>
                <option value="退貨/退款" ${
                  order.status === "退貨/退款" ? "selected" : ""
                }>退貨/退款</option>
                <option value="已完成" ${
                  order.status === "已完成" ? "selected" : ""
                }>已完成</option>
                <option value="取消" ${
                  order.status === "取消" ? "selected" : ""
                }>取消</option>
              </select>
            </div>
          </div>
          `,
    focusConfirm: false,
    showCancelButton: true,
    confirmButtonText: "修改",
    preConfirm: () => {
      // 收集表單資料並返回
      return {
        orderId: document.getElementById("orderId").value,
        pickupStore: document.getElementById("pickupStore").value,
        orderDate: document.getElementById("orderDate").value,
        paymentMethod: document.getElementById("paymentMethod").value,
        pickupDate: document.getElementById("pickupDate").value,
        status: document.getElementById("status").value,
      };
    },
  });
  // 如果表單資料存在，發送更新請求
  if (formValues) {
    try {
      const response = await axios.post(
        "http://localhost:8081/orders/update",
        formValues,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      // 處理回應
      if (response.data) {
        Swal.fire("成功", "訂單已更新", "success");
        fetchOrders(); // 更新後刷新訂單列表
      } else {
        Swal.fire("失敗", "更新失敗", "error");
      }
    } catch (error) {
      Swal.fire("錯誤", "更新訂單時發生錯誤", "error");
    }
  }
};

//修改項目
const handleUpdateItem = async (item) => {
  try {
    const payload = {
      id: item.id.toString(),
      size: item.size.toString(),
      color: item.color.toString(),
      quantity: item.quantity.toString(),
      unitPrice: item.unitPrice.toString(),
      discountAmount: item.discountAmount.toString(),
    };

    const response = await axios.post(
      "http://localhost:8081/orderItems/update",
      payload
    );

    Swal.fire("成功", "訂單項目已更新", "success");
    // 刷新所有訂單數據
    await fetchOrders();

    // 保持當前選擇的訂單項目視圖更新
    if (selectedMemberId.value) {
      await orderItemList(selectedMemberId.value);
    }
  } catch (error) {
    console.error("更新失敗：", error.response?.data || error.message);
    Swal.fire("錯誤", "更新訂單項目失敗", "error");
  }
};

//新增項目
const handleInsertItem = async (payload) => {
  try {
    console.log("新增商品 Payload:", payload); // 打印調試

    await axios.post("http://localhost:8081/orderItems/insert", payload, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    Swal.fire("成功", "訂單項目已新增", "success");

    // 刷新所有訂單數據
    fetchOrders();

    // 保持當前選擇的訂單項目視圖更新
    if (selectedMemberId.value) {
      orderItemList(selectedMemberId.value);
    }
  } catch (error) {
    console.error("新增失敗：", error.response?.data || error.message);
    Swal.fire("錯誤", "新增訂單項目失敗", "error");
  }
};

//刪除項目
const handleDeleteItem = async (item) => {
  try {
    await axios.delete(`http://localhost:8081/orderItems/delete/${item.id}`);
    Swal.fire("成功", "訂單項目已刪除", "success");
    orderItemList(selectedMemberId.value); // 刷新訂單項目
  } catch (error) {
    Swal.fire("錯誤", "刪除失敗", "error");
  }
};
</script>

<style lang="css" scoped>
#ordersTable {
  width: 85%;
  margin: 20px auto;
  border-collapse: collapse;
  background: #ffffff;
  border: 1px solid #ddd;
}

th,
td {
  padding: 30px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

button {
  padding: 12px;
  cursor: pointer;
  font-size: 25px;
  color: #666666;
}
button:hover {
  color: #c48888;
}

/* 分頁選項樣式 */
.dataTables_wrapper .dataTables_length select,
.dataTables_wrapper .dataTables_filter input {
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 5px;
  font-size: 14px;
}

/* 分頁按鈕樣式 */
.dataTables_wrapper .dataTables_paginate .paginate_button {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #d98888;
  color: white;
  cursor: pointer;
  margin: 0 2px;
  transition: background-color 0.3s ease;
}

/* 滑過分頁按鈕 */
.dataTables_wrapper .dataTables_paginate .paginate_button:hover {
  background-color: #bf4d4d;
}

/* 當前分頁按鈕 */
.dataTables_wrapper .dataTables_paginate .paginate_button.current {
  background-color: #bf4d4d;
  color: white;
  font-weight: bold;
}
</style>
