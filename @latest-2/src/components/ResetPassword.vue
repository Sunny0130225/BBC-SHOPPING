<script setup>
import { ref } from "vue";
import axios from "axios";
import Swal from "sweetalert2";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const token = ref(route.query.token);
const newPassword = ref("");
const isSubmitting = ref(false);

const resetPassword = async () => {
  if (!newPassword.value) {
    Swal.fire("錯誤", "請輸入新密碼", "error");
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await axios.post(
      "http://localhost:8081/api/auth/reset-password",
      {
        token: token.value,
        newPassword: newPassword.value,
      }
    );

    if (response.data.success) {
      Swal.fire("成功", "密碼重設成功，請使用新密碼登入", "success").then(
        () => {
          router.push("/login");
        }
      );
    } else {
      Swal.fire("錯誤", response.data.message, "error");
    }
  } catch (error) {
    Swal.fire("錯誤", "密碼重設失敗", "error");
    console.log("question" + error);
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<template>
  <div class="position">
    <h2>重設密碼</h2>

    <form @submit.prevent="resetPassword">
      <input
        type="password"
        v-model="newPassword"
        placeholder="請輸入新密碼"
        class="form-control"
        style="width: 500px; display: inline"
        required
      />
      <button :disabled="isSubmitting" type="submit" class="pinkbtn mt-3">
        {{ isSubmitting ? "重設中..." : "確認重設" }}
      </button>
    </form>
  </div>
</template>
<style scope>
.position {
  padding: 100px;
  text-align: center;
}
</style>
