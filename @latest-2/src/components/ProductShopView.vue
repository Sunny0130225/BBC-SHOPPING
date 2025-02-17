<template>
  <div>
    <div class="container">
      <!-- 左側分類列表 -->
      <div class="sidebar">
        <ul>
          <!-- 動態生成分類 -->
          <li v-for="(type, index) in types" :key="index">
            <div @click="fetchSubCategories(type.endpoint)">
              {{ type.name }}
            </div>
            <!-- 動態生成次分類 -->
            <ul v-if="type.expanded">
              <li
                v-for="subCategory in subCategories"
                :key="subCategory"
                @click="fetchProducts(type.endpoint, subCategory)"
              >
                {{ subCategory }}
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <!-- 右側瀑布流 -->
    <div class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img :src="product.image" alt="product.name" />
        <h3>{{ product.name }}</h3>
        <p>{{ product.price }}</p>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      // 分類類型和對應的 API
      types: [
        { name: "館別", endpoint: "/products/depart", expanded: false },
        { name: "類別", endpoint: "/products/category", expanded: false },
        { name: "季節", endpoint: "/products/season", expanded: false },
        { name: "風格", endpoint: "/products/style", expanded: false },
      ],
      // 動態次分類
      subCategories: [],
      // 產品資料
      products: [],
    };
  },
  methods: {
    // 獲取次分類（假設後端返回次分類列表）
    fetchSubCategories(endpoint) {
      // 找到點擊的類型，切換展開狀態
      const type = this.types.find((type) => type.endpoint === endpoint);
      type.expanded = !type.expanded;

      // 如果展開，調用後端獲取次分類數據
      if (type.expanded) {
        axios.get(endpoint).then((response) => {
          this.subCategories = response.data; // 假設後端返回次分類列表
        });
      } else {
        this.subCategories = []; // 收起時清空次分類
      }
    },
    // 獲取產品數據
    fetchProducts(endpoint, subCategory) {
      axios
        .get(endpoint, { params: { value: subCategory } }) // API 請求
        .then((response) => {
          this.products = response.data; // 更新產品資料
        });
    },
  },
};
</script>

<style>
.container {
  display: flex;
}

.sidebar {
  width: 20%;
  border-right: 1px solid #ccc;
  padding: 1em;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  cursor: pointer;
  padding: 0.5em 0;
  font-weight: bold;
}

.sidebar ul li ul li {
  cursor: pointer;
  padding: 0.5em 1em;
  font-weight: normal;
}

.product-grid {
  width: 80%;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1em;
  padding: 1em;
}

.product-card {
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 1em;
  text-align: center;
}

.product-card img {
  width: 100%;
  height: auto;
}
</style>
