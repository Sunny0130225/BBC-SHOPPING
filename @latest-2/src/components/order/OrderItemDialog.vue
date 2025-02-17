<template>
    <div class="overlay">
    <div class="dialog">
      <div class="dialodIndiv">
        <div class="header" style="width: 85%">
            <h4>訂單細項 - 會員ID: {{ memberId }}</h4>
            <button @click="addItem"
                    style="border: 2px #c48888 solid; border-radius: 10px; font: 1em sans-serif; color: white; background-color: #c48888;"
                    >新增商品</button>
            <button @click="$emit('closeDialog')" class="bi bi-x-circle"></button>
        </div>
        <table style="width: 85%">
          <thead>
            <tr>
              <th>商品ID</th>
              <th>商品名稱</th>
              <th>尺寸</th>
              <th>顏色</th>
              <th>數量</th>
              <th>單價</th>
              <th>折扣金額</th>
              <th>總金額</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in orderItems" :key="item.id">
              <td>{{ item.productBean.id }}</td>
              <td>{{ item.productBean.name }}</td>
              <td>{{ item.productBean.productDetails?.[0]?.size || '無尺寸' }}</td>
              <td>{{ item.productBean.productDetails?.[0]?.color || '無顏色' }}</td>
              <!-- <td>{{ item.productBean.productDetails[0].size }}</td>
              <td>{{ item.productBean.productDetails[0].color }}</td> -->
              <td>{{ item.quantity }}</td>
              <td>{{ item.unitPrice }}</td>
              <td>{{ item.discountAmount }}</td>
              <td>{{ item.quantity * item.unitPrice - item.discountAmount }}</td>
              <td>
                <button @click="editItem(item)" class="bi bi-pencil"></button>
                <button @click="deleteItem(item)" class="bi bi-trash3"></button>
              </td>
            </tr>
          </tbody>
        </table>

      </div>
    </div>
    </div> 
  </template>
  
  <script setup>
  import { computed, ref } from "vue";
  import Swal from "sweetalert2";
  
  // Props
  const props = defineProps({

    orderId: {
    type: Number,
    required: true,
    },

    orderItems: {
      type: Array,
      default: () => [],
    },
    memberId: {
      type: Number,
      required: true,
    },
  });
  
  // Emit
  const emit = defineEmits(["closeDialog", "updateItem", "insertItem","deleteItem"]);
  
  // 新增訂單項目
  const addItem = async () => {
    const { value: formValues } = await Swal.fire({
      title: "新增訂單項目",
      html: `
        <div>
            <label for="productId">商品ID:</label>
            <input id="productId" type="text" class="swal2-input" >
        </div>
        <div>
            <label for="size">尺寸:</label>
            <input id="size" type="text" class="swal2-input" >
        </div>
        <div>
            <label for="color">顏色:</label>
            <input id="color" type="text" class="swal2-input" >
        </div>
        <div>
            <label for="quantity">數量:</label>
            <input id="quantity" type="number" class="swal2-input" >
        </div>
        <div>
            <label for="unitPrice">單價:</label>
            <input id="unitPrice" type="number" class="swal2-input" >
        </div>
        <div>
            <label for="discountAmount">折扣金額:</label>
            <input id="discountAmount" type="number" class="swal2-input" >
        </div>
      `,
      focusConfirm: false,
      showCancelButton: true,
      preConfirm: () => {
          return {
            productId: document.getElementById("productId").value,
            size: document.getElementById("size").value,
            color: document.getElementById("color").value,
            quantity: parseInt(document.getElementById("quantity").value, 10),
            unitPrice: parseFloat(document.getElementById("unitPrice").value),
            discountAmount: parseFloat(document.getElementById("discountAmount").value),
        
        };
      },
    });
  
    if (formValues) {
      // 組裝完整數據
        const payload = {
        memberId: props.memberId,
        orderId: props.orderId,
        productId: formValues.productId,
        orderItem: {
        size: formValues.size,
        color: formValues.color,
        quantity: formValues.quantity,
        unitPrice: formValues.unitPrice,
        discountAmount: formValues.discountAmount,
      },
    };
      emit("insertItem", payload); // 通知父層新增
    }
  };
  
  // 編輯訂單項目
  const editItem = async (item) => {
    const { value: formValues } = await Swal.fire({
      title: "編輯訂單項目",
      html: `
        <div>
            <label for="size">尺寸:</label>
            <input id="size" type="text" class="swal2-input" value="${item.productBean.productDetails[0].size}" >
        </div>
        <div>
            <label for="color">顏色:</label>
            <input id="color" type="text" class="swal2-input" value="${item.productBean.productDetails[0].color}" >
        </div>
        <div>
            <label for="quantity">數量:</label>
            <input id="quantity" type="number" class="swal2-input" value="${item.quantity}" >
        </div>
        <div>
            <label for="unitPrice">單價:</label>
            <input id="unitPrice" type="number" class="swal2-input" value="${item.unitPrice}" >
        </div>
        <div>
            <label for="discountAmount">折扣金額:</label>
            <input id="discountAmount" type="number" class="swal2-input" value="${item.discountAmount}" >
        </div>
       `,
      focusConfirm: false,
      showCancelButton: true,
      preConfirm: () => {
        return {
          id: item.id,
          size: document.getElementById("size").value,
          color: document.getElementById("color").value,
          quantity: document.getElementById("quantity").value,
          unitPrice: document.getElementById("unitPrice").value,
          discountAmount: document.getElementById("discountAmount").value,
        };
      },
    });
  
    if (formValues) {
      emit("updateItem", { ...item, ...formValues }); // 通知父層修改
    }
  };
  
  // 刪除訂單項目
  const deleteItem = (item) => {
    Swal.fire({
      title: "確認刪除?",
      text: `您確定要刪除產品 ID: ${item.productBean.id} 嗎?`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "是",
      cancelButtonText: "否",
    }).then((result) => {
      if (result.isConfirmed) {
        emit("deleteItem", item); // 通知父層刪除
      }
    });
  };

  </script>
  
  <style scoped>
      h4 {
      margin: 0; 
      flex-grow: 1; 
      display: flex;
      }

    .header{
        display: flex; /* 使用 Flexbox 排版 */
        align-items: center; 
        justify-content: center; 
        gap: 10px; 
    }
    
    .dialog {
      justify-content: center;
      padding: 20px;
      background: #ececec;
      border: 10px solid #ececec;
      border-radius: 10px;
      width: 85%
    }
    
    .dialodIndiv{
       display: flex;
       flex-direction: column;
       text-align: center;
       align-items: center;
    }

    th, td {
    padding: 5px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  .overlay{
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

  button{
    padding: 10px;    
    cursor: pointer;
    font-size: 25px;
    color: #666666;
  }

  button:hover {
    color: #C48888;
  }

  </style>
  