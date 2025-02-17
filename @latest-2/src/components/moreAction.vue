<template>
  <div>
    <h1>圖片管理</h1>
    
    <!-- 上傳圖片 -->
    <div class="upload-container">
      <button class="upload-btn" @click="promptUpload">上傳圖片</button>
    </div>

    <!-- 圖片表格 -->
    <div class="table-container">
      <table id="imageTable" class="display" style="width:100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>商品 ID</th>
            <th>圖片</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2"; // ✅ 引入 SweetAlert2
import "datatables.net";
import "datatables.net-dt";

export default {
  name: "ProductImageView",
  data() {
    return {
      table: null,
    };
  },
  mounted() {
    this.initializeTable();
  },
  methods: {
    // 🔹 初始化圖片表
    initializeTable() {
      this.table = $("#imageTable").DataTable({
        ajax: async (data, callback) => {
          try {
            console.log("📡 嘗試獲取圖片數據");
            const response = await axios.get("http://localhost:8081/api/productImages"); // ✅ 確保 API URL 正確
            console.log("✅ API 回應:", response.data);
            callback({ data: response.data });
          } catch (error) {
            console.error("❌ 獲取圖片數據失敗", error);
          }
        },
        columns: [
          { data: "pictureId" },
          { data: "productId" }, // ✅ 新增商品 ID
          {
            data: "imageBase64",
            render: (data) =>
              data
                ? `<img src="data:image/png;base64,${data}" class="product-image" alt="圖片"/>`
                : "沒有圖片",
          },
          {
            data: "pictureId",
            render: (imageId) =>
              `<button class="delete-btn" data-image-id="${imageId}">刪除</button>`,
          },
        ],
        paging: false,
        searching: false,
        info: false,
        ordering: false,
      });

      // 🔹 綁定刪除按鈕事件
      $("#imageTable tbody").on("click", ".delete-btn", async (event) => {
        const imageId = $(event.currentTarget).data("image-id");
        await this.deleteImage(imageId);
      });
    },

    // 🔹 上傳圖片時 SweetAlert2 提示輸入商品 ID
    async promptUpload() {
      const { value: productId } = await Swal.fire({
        title: "輸入商品 ID",
        input: "number",
        inputPlaceholder: "請輸入商品 ID",
        showCancelButton: true,
        confirmButtonText: "確定",
        cancelButtonText: "取消",
      });

      if (!productId) return;

      const { value: file } = await Swal.fire({
        title: "選擇圖片",
        input: "file",
        inputAttributes: {
          accept: "image/*",
        },
        showCancelButton: true,
        confirmButtonText: "上傳",
        cancelButtonText: "取消",
      });

      if (file) {
        this.uploadImage(file, productId);
      }
    },

    // 🔹 上傳圖片（轉 Base64 並發送 API）
    async uploadImage(file, productId) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = async () => {
        try {
          const base64Data = reader.result.split(",")[1]; // 取出 Base64 純數據部分
          const payload = { imageBase64: base64Data, productId };

          console.log("📡 發送圖片上傳請求");
          await axios.post("http://localhost:8081/api/productImages/upload", payload, {
            headers: { "Content-Type": "application/json" },
          });

          console.log("✅ 圖片上傳成功");
          Swal.fire("成功！", "圖片已成功上傳", "success");
          this.table.ajax.reload(null, false); // ✅ 重新加載 DataTable
        } catch (error) {
          console.error("❌ 上傳圖片失敗", error);
          Swal.fire("錯誤！", "圖片上傳失敗，請稍後再試", "error");
        }
      };
    },

    // 🔹 刪除圖片（呼叫 API）
    async deleteImage(imageId) {
      try {
        console.log(`📡 嘗試刪除圖片 ID: ${imageId}`);
        await axios.delete(`http://localhost:8081/api/productImages/image/${imageId}`);

        console.log("✅ 圖片刪除成功");
        Swal.fire("成功！", "圖片已成功刪除", "success");
        this.table.ajax.reload(null, false); // ✅ 重新加載 DataTable
      } catch (error) {
        console.error(`❌ 刪除圖片 ${imageId} 失敗`, error);
        Swal.fire("錯誤！", "圖片刪除失敗", "error");
      }
    },
  },
};
</script>

<style>
/* 圖片樣式 */
.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 5px;
  margin: 5px;
}

/* 表格樣式 */
.table-container {
  margin-top: 20px;
}

/* 上傳按鈕 */
.upload-container {
  margin-bottom: 15px;
}

.upload-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
}

.upload-btn:hover {
  background-color: #45a049;
}

/* 刪除按鈕 */
.delete-btn {
  background-color: #ff4d4d;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.delete-btn:hover {
  background-color: #cc0000;
}
</style>