<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
import Swal from "sweetalert2";

const route = useRoute();
const memberId = route.params.id;
const role = ref(sessionStorage.getItem("role"));
const isReadonly = memberId === sessionStorage.getItem("memberId");

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

let details = ref({});
let originalDetails = ref({});
let member = ref({});
// 資料修改
// 標記表單是否被修改
let dataModified = ref(false);

onMounted(() => {
  axios
    .get(`/member/search/${memberId}`)
    .then((resp) => {
      if (resp.data != null) {
        member.value = resp.data;
      } else {
        console.log("會員id搜尋為null");
      }
    })
    .catch((err) => {
      console.log("會員id搜尋錯誤" + err);
    });
  axios
    .get(`/member/searchDetails/${memberId}`)
    .then((response) => {
      details.value = response.data || defaultDetails;
      axios
        .post(`/member/updateDetails/${memberId}`, details.value)
        .then((resp) => {
          if (resp.data) {
            originalDetails.value = JSON.parse(JSON.stringify(details.value));
            dataModified.value = false;
          } else {
            Swal.fire("失敗", "詳細資訊更新失敗", "error");
          }
        })
        .catch((err) => {
          Swal.fire("錯誤", "詳細資訊更新錯誤", "warning");
          console.log("error" + err);
        });
    })
    .catch((error) => {
      console.error("請求失敗：", error);
    });
});
const defaultPicture = "http://localhost:8081/upload/default.jpg";

const saveDetails = () => {
  axios
    .post(`/member/updateDetails/${memberId}`, details.value)
    .then((resp) => {
      if (resp.data) {
        Swal.fire("成功", "詳細資訊已更新", "success");
        originalDetails.value = JSON.parse(JSON.stringify(details.value));
        dataModified.value = false;
      } else {
        Swal.fire("失敗", "詳細資訊更新失敗", "error");
      }
    })
    .catch((err) => {
      Swal.fire("錯誤", "詳細資訊更新錯誤", "warning");
      console.log("error" + err);
    });
};

// 監聽 details 的變化
watch(
  details,
  (newDetails) => {
    dataModified.value =
      JSON.stringify(newDetails) !== JSON.stringify(originalDetails.value);
  },
  { deep: true } // 深度監聽，追蹤對象內部的屬性變化
);
// 禁用按鈕

const switchbtn = () => {
  saveDetails();
};
// 生成假資料
const generateFakeData = () => {
  details.value.realname = "林測試"; // 假本名
  details.value.gender = "男"; // 隨機性別
  details.value.birthday = "1998-11-23";
  details.value.phone = "0985254165"; // 台灣手機號
  details.value.address = "新北市土城區仁愛路117巷28號1樓"; // 假地址
};
</script>

<template>
  <span>
    <div class="container mt-4">
      <!-- 性別 -->
      <form>
        <img
          :src="details.picture || defaultPicture"
          alt="會員照片"
          class="member-photo"
          @click="triggerFileInput"
        />

        <!-- 提示文字 -->
        <div v-if="dataModified" class="save-reminder">
          請記得點擊「保存資料」以儲存更改！
        </div>
        <!-- 禁用按鈕 -->

        <div
          class="form-check form-switch"
          style="margin-left: 50px; padding: 0%"
        >
          <label
            class="form-check-label me-2"
            for="flexSwitchCheckDefault"
            style="margin: 3px"
            >{{ details.verified ? "啟用" : "禁用" }}</label
          >

          <input
            class="form-check-input"
            type="checkbox"
            id="flexSwitchCheckDefault"
            v-model="details.verified"
            @change="switchbtn()"
          />
        </div>

        <!-- 標籤和等級數值 -->
        <div class="row justify-content-center mb-3">
          <div class="col-12 col-md-8">
            <h5 style="text-align: center">{{ member.username }}</h5>
            <!-- 角色 -->
            <label for="membership-role" class="form-label me-2"
              >會員權限：</label
            >
            <select
              id="membership-role"
              class="form-select"
              aria-label="Default select example"
              v-model="details.role"
              style="margin: 5px"
              :disabled="role !== 'superadmin' || isReadonly"
            >
              <option value="">一般會員</option>
              <option value="admin">管理者</option>
              <option value="superadmin">最高管理者</option>
            </select>

            <!-- 會員等級標籤 -->
            <!-- <label for="membership-level" class="form-label me-2"
              >會員等級：</label
            > -->
            <!-- 下拉選單 -->
            <!-- <select
              id="membership-level"
              class="form-select"
              aria-label="Default select example"
              v-model="details.status"
              style="margin: 5px"
            >
              <option value="">青銅BBC</option>
              <option value="1">白銀BBC</option>
              <option value="2">黃金BBC</option>
              <option value="3">白金BBC</option>
            </select> -->
            <!-- 本名 -->
            <label for="realname" class="form-label">本名：</label>
            <input
              id="realname"
              type="text"
              class="form-control"
              placeholder="本名"
              v-model="details.realname"
            />
            <!-- 性別 -->
            <label for="gender" class="form-label me-2">性別：</label>
            <!-- 下拉選單 -->
            <select
              id="membership-level"
              class="form-select"
              aria-label="Default select example"
              v-model="details.gender"
              style="margin: 5px"
            >
              <option value="" disabled>請選擇性別</option>
              <option value="男">男</option>
              <option value="女">女</option>
              <option value="其他">其他</option>
            </select>

            <!-- 生日 -->

            <label for="birthday" class="form-label">生日：</label>
            <input
              id="birthday"
              type="date"
              class="form-control"
              placeholder="生日"
              v-model="details.birthday"
            />

            <!-- 電話 -->

            <label for="phone" class="form-label">電話：</label>
            <input
              id="phone"
              type="text"
              class="form-control"
              placeholder="電話"
              v-model="details.phone"
            />

            <!-- 地址 -->

            <label for="address" class="form-label">地址：</label>
            <input
              id="address"
              type="text"
              class="form-control"
              placeholder="地址"
              v-model="details.address"
            />
            <!-- 生成假資料按鈕 -->
            <button class="pinkbtn" type="button" @click="generateFakeData">
              生成假資料
            </button>
          </div>
        </div>
        <!-- 保存按鈕 -->
        <div class="row justify-content-center">
          <div class="col-12 col-md-8 text-center">
            <button type="button" class="pinkbtn" @click="saveDetails()">
              保存
            </button>
          </div>
        </div>
      </form>
    </div>
  </span>
</template>

<style lang="css" scoped>
.member-photo {
  display: inline;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin: 20px;
  cursor: pointer; /* 點擊手勢 */
}
.save-reminder {
  display: inline;
  background-color: #b6606042;
  color: #ec0621;
  padding: 10px 15px; /* 内边距，增加可读性 */
  border-radius: 5px; /* 圆角边框，增加视觉柔和感 */
  font-weight: bold; /* 加粗文字，增加重要性 */
  margin: 10px 10px 10px 0px; /* 与其他内容保持一定距离 */
  text-align: center; /* 居中文本 */
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); /* 轻微阴影，增加立体感 */
}
label {
  margin: 5px;
}
</style>
