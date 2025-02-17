<script setup></script>

<template>
  <div class="coupon-container">
    <h1>我的折價券</h1>
    <table class="coupon-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>名稱</th>
          <th>折扣</th>
          <th>限制</th>
          <th>使用次數</th>
          <th>到期日</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="coupon in coupons" :key="coupon.ID">
          <td>{{ coupon.ID }}</td>
          <td>{{ coupon.名稱 }}</td>
          <td>{{ coupon.折扣 }}</td>
          <td>{{ coupon.限制 }}</td>
          <td>{{ coupon.使用次數 }}</td>
          <td>{{ coupon.到期日 }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class="lucky-wheel" @click="goToLuckyDraw">
    <p>幸運輪盤</p>
  </div> 
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      coupons: [], // 儲存折價券數據
    };
  },
  async mounted() {
    const memberId = sessionStorage.getItem("memberId");

    try {
      const response = await axios.get("/api/membership-discount/coupons", {
        params: { memberId },
      });
      this.coupons = response.data.map(coupon => {
      const date = new Date(coupon.到期日);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');

      return {
        ...coupon,
        到期日: `${year}/${month}/${day}　${hours}:${minutes}`
      };
    });
    } catch (error) {
      console.error("獲取折價券失敗", error);
    }
  },
  methods: {
    goToLuckyDraw() {
      this.$router.push('/luckydraw'); // 跳转到 /luckydraw
    },
  },
};
</script>

<style lang="css" scoped></style>
<style scoped>
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

.lucky-wheel {
  position: fixed; /* 固定位置 */
  bottom: 20px; /* 距离底部 20px */
  right: 20px; /* 距离左侧 20px */
  width: 100px; /* 设置宽度 */
  height: 100px; /* 设置高度 */
  background: #c48888; /* 渐变背景 */
  border-radius: 50%; /* 圆形按钮 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3); /* 添加阴影 */
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: black;
  user-select: none;
  transition: transform 0.2s;
}

.lucky-wheel:hover {
  transform: scale(1.1);
}
</style>
