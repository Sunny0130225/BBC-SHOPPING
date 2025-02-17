<template>
  <div>
    <div class="page-container">
      <div class="container content-container">
        <!-- 左側篩選欄 -->
        <div class="sidebar">
          <div class="search-box">
            <input
              type="text"
              v-model="searchText"
              placeholder="請輸入關鍵字搜尋"
              @input="filterProducts"
              class="form-control"
            />
          </div>

          <div v-for="(category, index) in categories" :key="index">
            <div class="category-header" @click="toggleDropdown(index)">
              {{ category.name }}
            </div>
            <ul v-if="category.expanded" class="subcategory-list">
              <li
                v-for="sub in category.subcategories"
                :key="sub"
                @click="fetchProducts(category.type, sub)"
              >
                {{ sub }}
              </li>
            </ul>
          </div>
        </div>

        <!-- 右側商品列表 -->
        <div class="product-grid">
          <div
            v-for="product in filteredProducts"
            :key="product.id"
            class="product-card"
            @click="goToProductDetail(product.id)"
          >
            <h3>{{ product.name }}</h3>

            <!-- 顯示 Base64 商品圖片 -->
            <img
              v-if="product.picture"
              :src="getImageUrl(product.picture)"
              alt="商品圖片"
            />
            <p v-else>沒有商品圖片</p>

            <p>{{ product.depart }} | {{ product.category }}</p>
            <p>{{ product.season }} | {{ product.style }}</p>
            <p class="text-danger fw-bold">${{ product.price }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ProductShopView",
  data() {
    return {
      categories: [
        {
          name: "館別",
          type: "depart",
          expanded: false,
          subcategories: ["男裝", "女裝", "童裝", "其他"],
        },
        {
          name: "類別",
          type: "category",
          expanded: false,
          subcategories: [
            "上衣",
            "內衣",
            "外套",
            "背心",
            "長褲",
            "短褲",
            "帽子",
            "襪子",
            "其他",
          ],
        },
        {
          name: "季節",
          type: "season",
          expanded: false,
          subcategories: ["春季", "夏季", "秋季", "冬季", "其他"],
        },
        {
          name: "風格",
          type: "style",
          expanded: false,
          subcategories: ["日系", "韓系", "美系", "歐系", "其他"],
        },
      ],
      searchText: "",
      products: [],
      filteredProducts: [],
    };
  },
  methods: {
    getImageUrl(base64) {
      return base64
        ? `data:image/png;base64,${base64}`
        : "/assets/eto_hebi_fukubukuro.png";
    },
    goToProductDetail(productId) {
      this.$router.push({ name: "ProductDetail", params: { id: productId } });
    },
    toggleDropdown(index) {
      this.categories[index].expanded = !this.categories[index].expanded;
    },
    fetchProducts(type = null, value = null) {
      let url = "/products";
      const params = {};

      if (type && value) {
        url = `/products/${type}`;
        params[type] = value;
      }

      axios
        .get(url, { params })
        .then((response) => {
          console.log("商品數據：", response.data);
          this.products = response.data;
          this.filteredProducts = this.products;
        })
        .catch((error) => {
          console.error("獲取商品失敗：", error);
        });
    },
    filterProducts() {
      const searchTerm = this.searchText.toLowerCase();
      this.filteredProducts = this.products.filter((product) => {
        return (
          product.name.toLowerCase().includes(searchTerm) ||
          product.depart.toLowerCase().includes(searchTerm) ||
          product.category.toLowerCase().includes(searchTerm) ||
          product.season.toLowerCase().includes(searchTerm) ||
          product.style.toLowerCase().includes(searchTerm)
        );
      });

      if (!searchTerm) {
        this.filteredProducts = this.products;
      }
    },
  },
  mounted() {
    this.fetchProducts();
  },
};
</script>

<style>
/* 設定背景色 */
.page-container {
  background-color: #d9b3b3;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 30px;
}

/* 主要內容區塊 */
.content-container {
  background-color: #fff;
  display: flex;
  width: 90%;
  max-width: 1200px;
  border-radius: 10px;
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

/* 側邊欄 */
.sidebar {
  flex: 1;
  border-right: 2px solid #ccc;
  padding: 20px;
}

/* 搜尋框 */
.search-box input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* 商品分類標題 */
.category-header {
  cursor: pointer;
  padding: 10px 0;
  font-weight: bold;
  font-size: 1.1rem;
  color: #333;
  transition: color 0.2s ease;
}

.category-header:hover {
  color: #007bff;
}

/* 子分類列表 */
.subcategory-list {
  list-style: none;
  padding-left: 15px;
}

.subcategory-list li {
  cursor: pointer;
  padding: 5px 0;
  color: #555;
}

.subcategory-list li:hover {
  color: #007bff;
}

/* 商品網格 */
.product-grid {
  flex: 4;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  padding: 20px;
}

/* 商品卡片 */
.product-card {
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.15);
}

/* 商品圖片 */
.product-card img {
  width: 100%;
  object-fit: cover;
  border-radius: 5px;
}
</style>
