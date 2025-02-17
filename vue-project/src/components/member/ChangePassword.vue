<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { onMounted, ref } from "vue";

let memberid = sessionStorage.getItem("memberId");
let member = ref({});
onMounted(() => {
  axios
    .get(`/member/search/${memberid}`)
    .then((resp) => {
      if (resp.data) {
        member.value = resp.data;
      } else {
        Swal.fire("失敗", "沒有此用戶", "error");
      }
    })
    .catch((err) => {
      Swal.fire("錯誤", "搜尋遇到錯誤", "warning");
    });
});
const changePassword = () => {
  axios
    .put("/member/update", member.value)
    .then((resp) => {
      if (resp.data == true) {
        Swal.fire("成功", "帳密已更新", "success");
      } else {
        Swal.fire("失敗", "沒有此用戶", "error");
      }
    })
    .catch((err) => {
      Swal.fire("錯誤", `帳密更新遇到錯誤${err}`, "warning");
    });
};
</script>

<template>
  <div class="position">
    <div>
      <label for="changeid" class="form-label">會員編號:</label>

      <input
        type="text"
        id="changeid"
        class="form-control"
        v-model="member.id"
        readonly
      />

      <label for="changeuname" class="form-label">用戶名:</label>

      <input
        type="text"
        id="changeuname"
        class="form-control"
        v-model="member.username"
      />

      <label for="changeemail" class="form-label">信箱:</label>

      <input
        type="text"
        id="changeemail"
        class="form-control"
        v-model="member.email"
      />

      <label for="changepassword" class="form-label">密碼:</label>

      <input
        type="text"
        id="changepassword"
        class="form-control"
        v-model="member.passwords"
      />
    </div>
    <div><button @click="changePassword" class="button">修改</button></div>
  </div>
</template>

<style lang="css" scoped>
.position {
  border: 5px #b6606042 solid;
  border-radius: 15px;
  margin: 30px;
  text-align: left;
  padding: 100px;
}
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
</style>
