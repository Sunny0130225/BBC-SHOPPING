<template>
  <div>
    <v-container width="85%">
      <button @click="createMember" class="pinkbtn">新增會員</button>
      <v-text-field
        v-model="search"
        label="搜尋"
        clearable
        class="mb-4"
      ></v-text-field>
      <v-data-table
        :items="members"
        :headers="head"
        :search="search"
        item-value="id"
        class="elevation-1"
      >
        <template v-slot:[`item.actions`]="{ item }">
          <v-btn icon @click="editMember(item)" class="pinkbtn">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-btn icon @click="deleteMember(item.id)" class="pinkbtn">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
          <!--點下按鈕顯示會員詳情-->
          <v-btn
            icon
            class="pinkbtn"
            :to="{ name: 'MemberDetail', params: { id: item.id } }"
          >
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </template>
      </v-data-table>
    </v-container>
  </div>
</template>
<script setup>
import axios, { formToJSON } from "axios";
import Swal from "sweetalert2";
import { onMounted, ref } from "vue";

const head = [
  { title: "ID", value: "id" },
  { title: "電子信箱", value: "email" },
  { title: "密碼", value: "passwords" },
  { title: "用戶名", value: "username" },
  { title: "操作", value: "actions", sortable: false },
];

const members = ref([]); // 初始化為空陣列
const search = ref();
const defaultDetails = {
  gender: "",
  birthday: "",
  phone: "",
  address: "",
  realname: "",
};

// 在組件refresh後 searchAllMembers
onMounted(() => {
  axios
    .get("/member/search")
    .then(async (response) => {
      members.value = response.data.map((member) => ({
        ...member,
        details: member.details || { ...defaultDetails },
      })); // 更新數據到 ref
    })
    .catch((error) => {
      console.error("請求失敗：", error);
    });
});
//修改member
const editMember = (member) => {
  Swal.fire({
    title: "更改用戶資料",
    html: `
          <table>
                  <tr><td><label for="changeid">會員編號:</label></td>
                      <td><input type="text" id="changeid" class="swal2-input" value="${member.id}" readonly></td>
                 </tr>
                <tr><td><label for="changeuname">用戶名:</label></td>
                    <td>
                         <input type="text" id="changeuname" class="swal2-input" value="${member.username}">
                </td></tr>
                <tr><td>
                         <label for="changeemail">信箱:</label>
                  </td>
                  <td>
                        <input type="text" id="changeemail" class="swal2-input" value="${member.email}" >
                  </td></tr>
                  <tr><td>
                          <label for="changepassword">密碼:</label>
                     </td>
                      <td>
                          <input type="text" id="changepassword" class="swal2-input" value="${member.passwords}" >
                  </td></tr>
           </table>

          `,
    focusConfirm: false,
    showCancelButton: true,
    confirmButtonText: "修改",
    preConfirm() {
      return {
        id: $("#changeid").val(),
        username: $("#changeuname").val(),
        email: $("#changeemail").val(),
        passwords: $("#changepassword").val(),
      };
    },
  }).then((result) => {
    if (result.isConfirmed) {
      axios
        .put("/member/update", result.value)
        .then((resp) => {
          if (resp.data == true) {
            Swal.fire("成功", "用戶資料已更新", "success");
            refreshTable();
          } else {
            Swal.fire("失敗", "沒有此用戶", "error");
          }
        })
        .catch(() => {
          Swal.fire("錯誤", "更新遇到錯誤", "warning");
        });
    }
  });
};
//刪除Member
const deleteMember = (id) => {
  axios
    .delete(`/member/delete/${id}`)
    .then((resp) => {
      if (resp.data == true) {
        Swal.fire("成功", "用戶資料已刪除", "success");
        refreshTable();
      } else {
        Swal.fire("失敗", "沒有此用戶", "error");
      }
    })
    .catch((err) => {
      Swal.fire("錯誤", "刪除遇到錯誤", "warning");
      console.log("123");

      console.log("err" + err + id);
    });
};
const refreshTable = () => {
  axios
    .get("/member/search")
    .then(async (response) => {
      members.value = response.data.map((member) => ({
        ...member,
        details: member.details || { ...defaultDetails },
      })); // 更新數據到 ref
    })
    .catch((error) => {
      console.error("請求失敗：", error);
    });
};
//新增會員
const createMember = () => {
  Swal.fire({
    title: "創建用戶",
    html: `
          <table style="margin-left: 55px;">

                <tr><td><label for="changeuname">用戶名:</label></td>
                    <td>
                         <input type="text" id="cuname" class="swal2-input">
                </td></tr>
                <tr><td>
                         <label for="changeemail">信箱:</label>
                  </td>
                  <td>
                        <input type="text" id="cemail" class="swal2-input" " >
                  </td></tr>
                  <tr><td>
                          <label for="changepassword">密碼:</label>
                     </td>
                      <td>
                          <input type="password" id="cpassword" class="swal2-input" >
                  </td></tr>
                   <tr>
              <td colspan="2" style="text-align: center;">
                <button id="generate-fake-data" type="button" class="pinkbtn" style="margin-top: 20px;">一鍵帶入</button>
              </td>
            </tr>
           </table>

          `,
    focusConfirm: false,
    showCancelButton: true,
    cancelButtonText: "取消", // 設置取消按鈕的文字
    confirmButtonText: "創建",
    confirmButtonColor: "#c48888",
    preConfirm() {
      return {
        username: $("#cuname").val(),
        email: $("#cemail").val(),
        passwords: $("#cpassword").val(),
      };
    },
    didOpen: () => {
      // 綁定生成假資料按鈕的事件
      document
        .getElementById("generate-fake-data")
        .addEventListener("click", () => {
          document.getElementById("cuname").value = "may";
          document.getElementById("cemail").value = "may@gmail.com";
          formToJSON;
          document.getElementById("cpassword").value = 111;
        });
    },
  }).then((result) => {
    if (result.isConfirmed) {
      axios
        .post("/member/insert", result.value)
        .then((resp) => {
          if (resp.data == true) {
            Swal.fire("成功", "用戶資料已創建", "success");
            refreshTable();
          } else {
            Swal.fire("失敗", "已有此用戶", "error");
          }
        })
        .catch((err) => {
          Swal.fire("錯誤", "創建遇到錯誤", "warning");
        });
    }
  });
};
</script>
<style scoped></style>
