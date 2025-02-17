<template>
    <div class="report-container">
      <!-- 按鈕區域 -->
      <div class="button-group">
        <button class="fetch-btn" @click="promptProductId">庫存圓餅圖</button>
        <button class="export-btn" @click="exportCSV">匯出所有商品 CSV</button>
      </div>
  
      <!-- 商品名稱 -->
      <h2 v-if="productName">{{ productName }} 的存貨分布</h2>
  
      <!-- 圓餅圖 -->
      <div class="chart-wrapper">
        <canvas v-if="chartData.length > 0" ref="pieChart"></canvas>
      </div>
    </div>
  </template>
  
  <script>
  import { Chart, registerables } from "chart.js";
  import axios from "axios";
  import Swal from "sweetalert2";
  import Papa from "papaparse"; // CSV 解析庫
  
  Chart.register(...registerables);
  
  export default {
    name: "ProductReport",
    data() {
      return {
        productId: null,
        productName: "",
        chartData: [],
        chartInstance: null,
      };
    },
    methods: {
      // 🟢 輸入商品 ID
      async promptProductId() {
        const { value: productId } = await Swal.fire({
          title: "輸入商品 ID",
          input: "number",
          inputPlaceholder: "請輸入商品 ID",
          showCancelButton: true,
          confirmButtonText: "確認",
          cancelButtonText: "取消",
        });
  
        if (productId) {
          this.fetchProductDetails(productId);
        }
      },
  
      // 🟢 獲取 API 數據
      async fetchProductDetails(productId) {
        try {
          const [detailsResponse, productResponse] = await Promise.all([
            axios.get(`/api/products/${productId}/details`),
            axios.get(`/api/products/${productId}`)
          ]);
  
          this.productName = productResponse.data.name;
          this.productId = productId;
          this.chartData = detailsResponse.data.map((item) => ({
            size: item.size,
            color: item.color,
            sum: item.sum,
          }));
  
          this.renderChart();
        } catch (error) {
          console.error("API 錯誤:", error);
          Swal.fire("錯誤", "無法獲取商品數據，請確認 ID 是否正確", "error");
        }
      },
  
      renderChart() {
  this.$nextTick(() => {
    if (!this.$refs.pieChart) {
      console.error("圓餅圖 Canvas 尚未準備好！");
      return;
    }

    if (this.chartInstance) {
      this.chartInstance.destroy();
    }

    const ctx = this.$refs.pieChart.getContext("2d");

    this.chartInstance = new Chart(ctx, {
      type: "pie",
      data: {
        labels: this.chartData.map((item) => `${item.size} (${item.color})`),
        datasets: [
          {
            data: this.chartData.map((item) => item.sum),
            backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF"],
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "right",
          },
        },
      },
    });
  });
},
  
      // 🟢 匯出所有商品 CSV
      async exportCSV() {
  try {
    const response = await axios.get("/api/products");
    const products = response.data;

    if (!products.length) {
      Swal.fire("錯誤", "沒有可匯出的商品資料！", "error");
      return;
    }

    // 🔹 轉換 JSON 為 CSV，展開 `productDetails`
    const csvData = products.flatMap(product => {
      if (!product.productDetails || product.productDetails.length === 0) {
        return [{
          id: product.id,
          name: product.name,
          depart: product.depart,
          category: product.category,
          season: product.season,
          style: product.style,
          introduction: product.introduction,
          price: product.price,
          size: "",   // 無 productDetails 時填空值
          color: "",
          sum: ""
        }];
      }

      // 🔹 逐個展開 productDetails
      return product.productDetails.map(detail => ({
        id: product.id,
        name: product.name,
        depart: product.depart,
        category: product.category,
        season: product.season,
        style: product.style,
        introduction: product.introduction,
        price: product.price,
        size: detail.size,
        color: detail.color,
        sum: detail.sum
      }));
    });

    const csv = Papa.unparse(csvData);
    const csvWithBOM = "\uFEFF" + csv;
    const blob = new Blob([csvWithBOM], { type: "text/csv;charset=utf-8;" });

    // 🔹 下載 CSV 檔案
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "商品與詳細資訊.csv";
    link.click();
  } catch (error) {
    console.error("無法獲取商品數據", error);
    Swal.fire("錯誤", "匯出失敗，請稍後再試！", "error");
  }
},
    },
  };
  </script>
  
  <style>
  .report-container {
    text-align: center;
    margin-top: 20px;
  }
  
  .button-group {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-bottom: 20px;
  }
  
  .fetch-btn, .export-btn {
    background-color: #C48888;;
    color: white;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .fetch-btn:hover {
    background-color: #0056b3;
  }
  
  .export-btn {
    background-color: #C48888;;
  }
  
  .export-btn:hover {
    background-color: #218838;
  }
  
  .chart-wrapper {
    width: 300px; /* ✅ 圖表縮小 */
    height: 300px;
    margin: auto;
  }
  </style>