<template>
  <div>
    <div class="member-center">
      <div class="imgDiv">
        <img
          :src="previewPhoto || photoUrl"
          alt="會員照片"
          class="member-photo"
          @click="triggerFileInput"
        />

        <div>您好，{{ username }}</div>

        <div>
          <input
            type="file"
            @change="handlePhotoUpload"
            ref="fileInput"
            style="display: none"
          />
        </div>

        <!-- 提醒用户保存的提示 -->
        <div v-if="showPhotoReminder" class="save-reminder">
          您有尚未保存的照片，請點擊保存按鈕！
        </div>

        <button @click="savePhoto" class="button">保存照片</button>
      </div>
      <!-- 會員 VIP 小卡 -->
      <!-- <div class="vip-card" :class="getVipClass">
        <h4>VIP 等級：{{ getVipLevel }}</h4>
      </div> -->
      <!-- 詳細資料表格 -->
      <div class="details-form">
        <form @submit.prevent="submitDetails">
          <h3>會員詳細資料</h3>
          <!-- 提示文字 -->
          <div v-if="dataModified" class="save-reminder">
            請記得點擊「保存資料」以儲存更改！
          </div>
          <div class="mb-3">
            <label for="realname" class="form-label">本名：</label>
            <input
              type="text"
              class="form-control"
              id="realname"
              v-model="details.realname"
              placeholder="請輸入您的本名"
            />
          </div>
          <label for="gender" class="form-label">性別：</label>
          <select id="gender" v-model="details.gender" class="form-control">
            <option value="男">男</option>
            <option value="女">女</option>
            <option value="其他">其他</option>
          </select>

          <label for="birthday" class="form-label">出生日期：</label>
          <input
            id="birthday"
            type="date"
            v-model="details.birthday"
            class="form-control"
          />

          <label for="phone" class="form-label">電話：</label>
          <input
            id="phone"
            type="text"
            v-model="details.phone"
            placeholder="請輸入您的電話"
            class="form-control"
          />

          <label for="address" class="form-label">地址：</label>
          <textarea
            id="address"
            v-model="details.address"
            placeholder="請輸入您的地址"
            class="form-control"
          ></textarea>
          <!-- 生成假資料按鈕 -->
          <button class="button" type="button" @click="generateFakeData">
            一鍵帶入
          </button>
          <button type="submit" class="button">保存資料</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import axios from "axios";
import Swal from "sweetalert2";

const fileInput = ref(null); //控制隱藏的input
const triggerFileInput = () => {
  fileInput.value.click(); //當點擊上傳圖片 就點擊隱藏的Input
};

let photoUrl = ref(`${axios.defaults.baseURL}/upload/default.jpg`); // 從後端獲取的會員照片 URL
let previewPhoto = ref(""); // 預覽圖片的 URL
// 保存照片到伺服器
let selectedPhoto = ref("");
let showPhotoReminder = ref(false);
let username = ref(sessionStorage.getItem("username"));

// 從伺服器獲取的會員照片
onMounted(() => {
  previewPhoto.value = null;
  axios
    .get(`/front/getImg/${sessionStorage.getItem("memberId")}`)
    .then((resp) => {
      photoUrl.value =
        resp.data || `${axios.defaults.baseURL}/upload/default.jpg`;
    })
    .catch((err) => {
      console.log("獲取圖片失敗" + err);
      photoUrl.value = `${axios.defaults.baseURL}/upload/default.jpg`;
    });
});

//選擇圖片時先讀取預覽圖片
const handlePhotoUpload = (event) => {
  const file = event.target.files[0]; //獲取 HTML <input type="file"> 元素中選擇的第一個文件url
  if (file) {
    selectedPhoto.value = file; //選擇的圖片檔案=>傳給保存圖片按鈕
    showPhotoReminder.value = true;
    // 使用 FileReader 生成圖片預覽 URL
    const reader = new FileReader();
    reader.onload = (e) => {
      previewPhoto.value = e.target.result; // 設置預覽圖片數據 src顯示預覽圖片
    };
    reader.readAsDataURL(file); //這一句執行時會回調 reader.onload返回 e.target.result()
  }
};

const savePhoto = async () => {
  if (!selectedPhoto.value) {
    alert("請先選擇照片！");
    return;
  }

  const formData = new FormData();
  formData.append("photo", selectedPhoto.value);
  formData.append("id", sessionStorage.getItem("memberId"));
  try {
    const response = await axios.post("/front/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    // 更新照片 URL
    photoUrl.value = response.data;
    previewPhoto.value = null;
    selectedPhoto.value = null;
    showPhotoReminder = false;
    console.log(response.data);

    Swal.fire("成功", "會員照片更新成功", "success").then(() =>
      window.location.reload()
    );
  } catch (error) {
    console.error("照片上傳失敗：", error);
    Swal.fire("失敗", "會員照片更新失敗", "error");
  }
};

//請求用戶詳細資料
const defaultDetails = {
  gender: "",
  birthday: "",
  phone: "",
  address: "",
  realname: "",
  status: "",
  role: "",
  verified: true,
};
const generateFakeData = () => {
  details.value.realname = "林測試"; // 假本名
  details.value.gender = "男"; // 隨機性別
  details.value.birthday = "1998-11-23";
  details.value.phone = "0985254165"; // 台灣手機號
  details.value.address = "新北市土城區仁愛路117巷28號1樓"; // 假地址
};
let details = ref({ ...defaultDetails });
let originalDetails = ref(null);
onMounted(() => {
  axios
    .get(`front/searchDetails/${sessionStorage.getItem("memberId")}`)
    .then((resp) => {
      details.value = resp.data || defaultDetails;
      originalDetails.value = JSON.parse(JSON.stringify(details.value)); // 保存初始值
    })
    .catch((err) => {
      console.log("獲取會員詳細資料有錯誤" + err);
    });
});
// 保存詳細資料memberI
const submitDetails = () => {
  axios
    .post(
      `/member/updateDetails/${sessionStorage.getItem("memberId")}`,
      details.value
    )
    .then((resp) => {
      if (resp.data) {
        Swal.fire("成功", "會員詳細資料更新成功", "success");
        originalDetails.value = JSON.parse(JSON.stringify(details.value));
        dataModified.value = false;
      } else {
        Swal.fire("失敗", "會員詳細資料更新失敗，請稍後再試", "error");
      }
    })
    .catch((err) => {
      Swal.fire("錯誤", "會員詳細資料更新時發生錯誤，請稍後再試", "warning");
    });
};
// 標記表單是否被修改
let dataModified = ref(false);

// 監聽 details 的變化
watch(
  details,
  (newDetails) => {
    dataModified.value =
      JSON.stringify(newDetails) !== JSON.stringify(originalDetails.value);
  },
  { deep: true } // 深度監聽，追蹤對象內部的屬性變化
);

// 根據 status 返回對應的 VIP 等級名稱

const getVipLevel = computed(() => {
  switch (details.value.status) {
    case "":
      return "青銅BBC";
    case "1":
      return "白銀BBC";
    case "2":
      return "黃金BBC";
    case "3":
      return "白金BBC";
  }
});
// 根據 status 設定卡片樣式
const getVipClass = computed(() => {
  switch (details.value.status) {
    case "":
      return "vip-basic";
    case "1":
      return "vip-silver";
    case "2":
      return "vip-gold";
    case "3":
      return "vip-platinum";
  }
});
</script>

<style scoped>
.member-center {
  border: 5px #b6606042 solid;
  border-radius: 15px;
  display: flex !important; /* 使用 Flexbox 進行布局 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between;
  margin: 30px;
  padding: 30px;
  height: 600px;
  display: block;
}
.imgDiv {
  width: 250px;
  height: 300px;
  text-align: center;
}
.member-photo {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin: 20px;
  cursor: pointer; /* 點擊手勢 */
}
.details-form {
  height: 600px;
  width: 800px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px; /* 與右側留出間距 */
  align-self: flex-start; /* 確保按鈕對齊容器的頂部 */
}

.form-control {
  width: 800px;
}
/* vip小卡架子 */
.vip-card {
  padding: 20px;
  border-radius: 10px;
  text-align: left;
  margin: 20px 0;
  height: 50%;
  width: 40%;
  color: #000000;
  font-weight: bold;
}
/* 不同等級的 VIP 樣式 */
.vip-basic {
  background-color: #f2e6e6;
}

.vip-silver {
  background-color: #e1c4c4;
}

.vip-gold {
  background-color: #cf9e9e;
}

.vip-platinum {
  background-color: #946767;
}
.save-reminder {
  background-color: #b6606042; /* 柔和的黄色背景 */
  color: #ec0621; /* 深棕色文字，和背景颜色对比鲜明 */
  padding: 10px 15px; /* 内边距，增加可读性 */
  border-radius: 5px; /* 圆角边框，增加视觉柔和感 */
  font-weight: bold; /* 加粗文字，增加重要性 */
  margin: 10px 10px 10px 0px; /* 与其他内容保持一定距离 */
  text-align: center; /* 居中文本 */
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); /* 轻微阴影，增加立体感 */
}
</style>
