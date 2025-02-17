<template>
  <div>
    <HeadLogin></HeadLogin>
    <div class="page-container">
      <!-- 主要商品內容 -->
      <div class="container content-container mt-5 p-4 rounded shadow">
        <!-- 商品內容（圖片 & 詳細資訊） -->
        <div class="row d-flex justify-content-center gap-5 mt-5">
          <div
            class="col-md-6 d-flex align-items-center justify-content-center position-relative"
          >
            <img
              v-if="productDetails.picture"
              :src="getImageUrl(productDetails.picture)"
              class="img-fluid rounded shadow"
            />
            <p v-else class="text-muted mt-3">沒有商品圖片</p>
          </div>

          <div class="col-md-6 mt-5">
            <h4 class="mb-3">
              商品名稱: {{ productDetails.name || "未提供" }}
            </h4>
            <p>
              <strong>館別:</strong> {{ productDetails.depart || "未提供" }}
            </p>
            <p>
              <strong>類別:</strong> {{ productDetails.category || "未提供" }}
            </p>
            <p>
              <strong>季節:</strong> {{ productDetails.season || "未提供" }}
            </p>
            <p><strong>風格:</strong> {{ productDetails.style || "未提供" }}</p>
            <p>
              <strong>商品價格:</strong>
              <span class="text-danger fs-4"
                >${{ productDetails.price || "未提供" }}</span
              >
            </p>
          </div>
        </div>

        <!-- 商品介紹 & 購買 -->
        <div class="row mt-5">
          <div class="col-12 mt-5">
            <h2>商品介紹</h2>
            <p class="text-muted">
              {{ productDetails.introduction || "無相關介紹" }}
            </p>
          </div>

          <div class="col-12 mt-5">
            <h2>商品購買</h2>
            <div
              v-if="
                !productDetails.productDetails ||
                productDetails.productDetails.length === 0
              "
            >
              <p class="text-muted">商品已售完，補貨中</p>
            </div>

            <table v-else class="table table-striped table-hover mt-3">
              <thead class="table-dark">
                <tr>
                  <th>尺寸</th>
                  <th>顏色</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="detail in productDetails.productDetails"
                  :key="detail.detailId"
                >
                  <td>{{ detail.size }}</td>
                  <td>{{ detail.color }}</td>
                  <td>
                    <button
                      class="btn btn-primary btn-sm"
                      @click.stop="
                        addToCart(
                          productDetails.id,
                          detail.detailId,
                          productDetails.price,
                          productDetails.name,
                          detail.size,
                          detail.color,
                          productDetails.picture
                        )
                      "
                    >
                      加入購物車
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 你可能會喜歡 (新的獨立容器) -->
      <div class="container related-products-container mt-5 p-4 rounded shadow">
        <h2 class="mb-3">你可能會喜歡</h2>
        <div class="related-products-scroll overflow-auto d-flex">
          <div
            v-for="product in relatedProducts"
            :key="product.id"
            class="related-product-card"
            @click="goToProductDetail(product.id)"
          >
            <img
              v-if="product.picture"
              :src="getImageUrl(product.picture)"
              class="related-product-img img-fluid rounded shadow"
            />
            <p v-else>沒有商品圖片</p>
            <h5 class="mt-2">{{ product.name }}</h5>
            <p class="text-danger fw-bold">${{ product.price }}</p>
          </div>
        </div>
      </div>
    </div>
    <FooterView></FooterView>
  </div>
</template>

<script>
import axios from "axios";

import Swal from "sweetalert2";
import FooterView from "@/views/FooterView.vue";
import HeadLogin from "@/views/HeadLogin.vue";
import { watch } from "vue";

export default {
  components: { HeadLogin, FooterView },

  props: ["id"],
  data() {
    return {
      productDetails: { images: [] },
      relatedProducts: [],
      currentImageIndex: 0,
    };
  },
  mounted() {
    this.fetchProductData();
  },
  watch: {
    "$route.params.id": function () {
      this.fetchProductData(); // 當 id 變更時重新獲取數據
    },
  },
  methods: {
    getImageUrl(base64) {
      return base64
        ? `data:image/png;base64,${base64}`
        : "/assets/default-image.png";
    },
    addToCart(id, detailId, price, name, size, color, picture) {
      console.log(
        "🛒 嘗試加入購物車，商品 ID:",
        id,
        "詳細 ID:",
        detailId,
        "價格:",
        price
      );

      const quantity = 1; // 預設數量
      const totalPrice = parseFloat(price) * quantity; // 計算總價
      const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true"; // 檢查是否登入
      const memberId = sessionStorage.getItem("memberId"); // 直接取值，不使用 JSON.parse()

      if (!isLoggedIn || !memberId || memberId === "null") {
        Swal.fire({
          title: "請先登入",
          text: "您需要登入才能加入購物車",
          icon: "warning",
        });
        return;
      }

      // ✅ 確保 `id` 和 `detailId` 來自函數參數，而不是 `this.productDetails`
      const cartItem = {
        productId: id, // 來自參數
        productDetail_id: detailId, // 來自參數
        depart: this.productDetails.depart, // 確保這個值存在
        quantity: quantity,
        totalPrice: totalPrice,
        member_id: memberId, // 會員 ID
        color: color,
        size: size,
        name: name,
        picture: picture,
      };

      console.log("📦 準備發送 API，購物車數據:", cartItem);

      axios
        .post(`/cartItem/add`, cartItem) // **確保 API 路徑正確**
        .then((response) => {
          console.log("✅ API 回應成功:", response.data);

          // 🔔 觸發通知
          Swal.fire({
            title: "成功！",
            text: "商品已加入購物車",
            icon: "success",
            timer: 1500,
            showConfirmButton: false,
          });
        })
        .catch((error) => {
          console.error("❌ API 回應錯誤:", error);

          Swal.fire({
            title: "錯誤！",
            text: "無法加入購物車，請稍後再試",
            icon: "error",
          });
        });
    },
    fetchProductData() {
      axios
        .get(`/products/${this.id}`)
        .then((response) => {
          this.productDetails = response.data || { images: [] };
          this.fetchRelatedProducts();
        })
        .catch((error) => console.error("獲取商品數據失敗：", error));
    },

    fetchRelatedProducts() {
      if (!this.productDetails.depart) return;

      axios
        .get(`/products/depart`, {
          params: { depart: this.productDetails.depart },
        })
        .then((response) => {
          this.relatedProducts = response.data.filter(
            (product) => product.id !== this.productDetails.id
          );
        })
        .catch((error) => console.error("獲取相關商品失敗：", error));
    },

    goToProductDetail(productId) {
      if (!productId) {
        console.error("無法跳轉，商品 ID 無效");
        return;
      }

      if (this.$route.params.id === productId.toString()) {
        console.log("相同商品，強制重整");
        this.$router
          .replace({ name: "ProductDetail", params: { id: productId } })
          .then(() => {
            this.fetchProductData(); // 重新載入商品資訊
          });
      } else {
        console.log("跳轉到商品 ID:", productId);
        this.$router.push({ name: "ProductDetail", params: { id: productId } });
      }
    },
  },
};
</script>

<style>
/* 讓整個背景顏色一致 */
.page-container {
  background-color: #d9b3b3;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 讓主要內容區塊有陰影 */
.content-container {
  background-color: #fff;
  width: 90%;
  max-width: 1200px;
}

/* 新增推薦商品的獨立容器 */
.related-products-container {
  background-color: #fff;
  width: 90%;
  max-width: 1200px;
}

/* 讓推薦商品橫向排列 */
.related-products-scroll {
  display: flex;
  gap: 15px;
  overflow-x: auto;
  white-space: nowrap;
  padding: 10px 0;
}

/* 推薦商品卡片 */
.related-product-card {
  flex: 0 0 200px;
  text-align: center;
  padding: 10px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.related-product-card:hover {
  transform: scale(1.05);
}

/* 推薦商品圖片 */
.related-product-img {
  width: 100%;
  object-fit: cover;
  border-radius: 5px;
}
</style>
