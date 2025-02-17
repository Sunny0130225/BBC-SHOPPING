<template>
  <div style="padding: 10px 150px 0px 150px">
    <h1>商品資訊與管理</h1>
    <div class="action-buttons">
      <button @click="addProduct" id="addProductBtn">新增商品</button>
      <button @click="morePower" id="morePowerBtn">其他功能</button>
    </div>

    <div class="table">
      <table id="productTable" class="display" style="width: 100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>圖片</th>
            <th>名稱</th>
            <th>館別</th>
            <th>分類</th>
            <th>季節</th>
            <th>風格</th>
            <th>價格</th>
            <th>操作</th>
            <th>展開</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";
import "datatables.net";
import "datatables.net-dt";

export default {
  name: "ProductView",
  data() {
    return {
      table: null, // 保存父表的 DataTable 實例
      subTables: {}, // 保存子表的 DataTable 實例
    };
  },
  mounted() {
    this.initializeMainTable();
  },
  methods: {
    async morePower() {
      this.$router.push({ name: "morePower" }); // 透過 Vue Router 跳轉
    },
    async addProduct() {
      const { value: formValues } = await Swal.fire({
        title: "新增商品",
        html: `
            <label for="new-name">商品名稱：</label>
            <input type="text" id="new-name" class="swal2-input" placeholder="輸入商品名稱">
            <label for="new-image">選擇圖片：</label>
            <input type="file" id="new-image" class="swal2-file">
            <label for="new-depart">館別：</label>
            <select id="new-depart" class="swal2-input">
                <option value="男裝">男裝</option>
                <option value="女裝">女裝</option>
                <option value="童裝">童裝</option>
                <option value="其他">其他</option>
            </select>
            <label for="new-category">類別：</label>
            <select id="new-category" class="swal2-input">
                <option value="上衣">上衣</option>
                <option value="內衣">內衣</option>
                <option value="外套">外套</option>
                <option value="背心">背心</option>
                <option value="長褲">長褲</option>
                <option value="短褲">短褲</option>
                <option value="帽子">帽子</option>
                <option value="襪子">襪子</option>
                <option value="其他">其他</option>
            </select>
            <label for="new-season">季節：</label>
            <select id="new-season" class="swal2-input">
                <option value="春季">春季</option>
                <option value="夏季">夏季</option>
                <option value="秋季">秋季</option>
                <option value="冬季">冬季</option>
                <option value="其他">其他</option>
            </select>
            <label for="new-style">風格：</label>
            <select id="new-style" class="swal2-input">
                <option value="日系">日系</option>
                <option value="韓系">韓系</option>
                <option value="美系">美系</option>
                <option value="歐系">歐系</option>
                <option value="其他">其他</option>
            </select>
            <label for="new-price">價格：</label>
            <input type="number" id="new-price" class="swal2-input" placeholder="輸入價格">
        `,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: "保存",
        cancelButtonText: "取消",
        preConfirm: () => {
          return new Promise((resolve) => {
            const name = document.getElementById("new-name").value;
            const depart = document.getElementById("new-depart").value;
            const category = document.getElementById("new-category").value;
            const season = document.getElementById("new-season").value;
            const style = document.getElementById("new-style").value;
            const price = document.getElementById("new-price").value;
            const fileInput = document.getElementById("new-image").files[0];

            if (
              !name ||
              !depart ||
              !category ||
              !season ||
              !style ||
              !price ||
              !fileInput
            ) {
              Swal.showValidationMessage("所有欄位都必須填寫！");
              return;
            }

            const reader = new FileReader(); // ✅ 正確宣告 FileReader
            reader.readAsDataURL(fileInput);

            reader.onload = () => {
              resolve({
                name,
                depart,
                category,
                season,
                style,
                price: parseFloat(price),
                picture: reader.result.split(",")[1], // ✅ 轉換 Base64
              });
            };

            reader.onerror = () => {
              Swal.showValidationMessage("圖片讀取失敗！");
            };
          });
        },
      });

      if (formValues) {
        try {
          await axios.post("/api/products", formValues, {
            headers: { "Content-Type": "application/json" },
          });
          Swal.fire("成功！", "商品已新增！", "success");
          this.refreshTable();
        } catch (error) {
          Swal.fire("錯誤！", "新增商品失敗！", "error");
        }
      }
    },
    // 初始化父表
    initializeMainTable() {
      this.table = $("#productTable").DataTable({
        ajax: async (data, callback) => {
          try {
            const response = await axios.get("/api/products");
            callback({ data: response.data });
          } catch (error) {
            Swal.fire("錯誤", "加載商品數據失敗", "error");
          }
        },
        columns: [
          { data: "id" },
          {
            data: "picture",
            render: (data) =>
              data
                ? `<img src="data:image/png;base64,${data}" class="product-image" alt="商品圖片" style="width: 60px; height: 60px; object-fit: cover; border-radius: 5px;"/>`
                : "沒有圖片",
          },
          { data: "name" },
          { data: "depart" },
          { data: "category" },
          { data: "season" },
          { data: "style" },
          { data: "price" },
          {
            data: null,
            render: (data) => `
            <button class="pinkbtn edit-btn" data-id="${data.id}">編輯</button>
        `,
          },
          {
            className: "dt-control",
            orderable: false,
            data: null,
            defaultContent: "",
          },
        ],
      });
      $("#productTable tbody").on("click", ".edit-btn", (event) => {
        const productId = $(event.currentTarget).data("id");
        this.showActionForm(productId);
      });
      // 綁定父表事件
      this.bindMainTableEvents();
    },
    async refreshTable() {
      try {
        const response = await axios.get("/api/products");
        this.table.clear(); // 清空當前表格數據
        this.table.rows.add(response.data); // 添加新數據
        this.table.draw(false); // 重繪表格
      } catch (error) {
        Swal.fire("錯誤", "刷新表格數據失敗", "error");
      }
    },
    showActionForm(productId) {
      Swal.fire({
        title: "商品操作",
        html: `
      <div id="action-form">
        <button class="pinkbtn action-btn edit-name-btn" data-id="${productId}">更改名稱</button>
        <button class="pinkbtn action-btn edit-depart-btn" data-id="${productId}">更改部門</button>
        <button class="pinkbtn action-btn edit-category-btn" data-id="${productId}">更改類別</button>
        <button class="pinkbtn action-btn edit-season-btn" data-id="${productId}">更改季節</button>
        <button class="pinkbtn action-btn edit-style-btn" data-id="${productId}">更改風格</button>
        <button class="pinkbtn action-btn edit-picture-btn" data-id="${productId}">更改圖片</button>
        <button class="pinkbtn action-btn edit-price-btn" data-id="${productId}">更改價格</button>
         <button class="pinkbtn action-btn insert-detail-btn" data-id="${productId}">新增詳細資料</button>
        <button class="pinkbtn action-btn delete-btn" data-id="${productId}">刪除</button>
      </div>
    `,
        showCancelButton: true,
        showConfirmButton: false,
        focusConfirm: false,
        cancelButtonText: "取消",
      });

      // 綁定每個按鈕的功能
      document
        .querySelector(".edit-name-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const { value: newName } = await Swal.fire({
            title: "更改名稱",
            input: "text",
            inputPlaceholder: "輸入新的名稱",
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newName) {
            try {
              await axios.patch(`/api/products/${productId}/name`, newName, {
                headers: { "Content-Type": "text/plain" },
              });
              Swal.fire("成功!", "名稱已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "名稱更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".edit-depart-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const departOptions = ["男裝", "女裝", "童裝", "其他"];
          const { value: newDepart } = await Swal.fire({
            title: "更改館別",
            input: "select",
            inputOptions: departOptions.reduce((obj, depart) => {
              obj[depart] = depart; // 將選項轉換為鍵值對格式
              return obj;
            }, {}),
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newDepart) {
            try {
              await axios.patch(
                `/api/products/${productId}/depart`,
                newDepart,
                {
                  headers: { "Content-Type": "text/plain" },
                }
              );
              Swal.fire("成功!", "館別已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "館別更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".edit-category-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const categoryOptions = [
            "上衣",
            "內衣",
            "外套",
            "背心",
            "長褲",
            "短褲",
            "帽子",
            "襪子",
            "其他",
          ];
          const { value: newCategory } = await Swal.fire({
            title: "更改類別",
            input: "select",
            inputOptions: categoryOptions.reduce((obj, category) => {
              obj[category] = category; // 將選項轉換為鍵值對格式
              return obj;
            }, {}),
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newCategory) {
            try {
              await axios.patch(
                `/api/products/${productId}/category`,
                newCategory,
                {
                  headers: { "Content-Type": "text/plain" },
                }
              );
              Swal.fire("成功!", "類別已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "類別更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".edit-picture-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;

          const { value: file } = await Swal.fire({
            title: "更改圖片",
            text: "請選擇新圖片",
            input: "file",
            inputAttributes: {
              accept: "image/*",
              "aria-label": "上傳新圖片",
            },
            showCancelButton: true,
            confirmButtonText: "上傳",
            cancelButtonText: "取消",
          });

          if (file) {
            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = async () => {
              const base64Image = reader.result.split(",")[1]; // 轉換成純 Base64 字串

              try {
                await axios.put(
                  `/api/products/${id}/picture`,
                  { picture: base64Image },
                  {
                    headers: { "Content-Type": "application/json" },
                  }
                );

                Swal.fire("成功!", "圖片已更新!", "success");
                this.refreshTable(); // ✅ 更新 DataTable
              } catch (error) {
                Swal.fire("錯誤!", "圖片更新失敗!", "error");
              }
            };

            reader.onerror = () => {
              Swal.fire("錯誤!", "無法讀取圖片", "error");
            };
          }
        });
      document
        .querySelector(".edit-season-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const seasonOptions = ["春季", "夏季", "秋季", "冬季", "其他"];
          const { value: newSeason } = await Swal.fire({
            title: "更改季節",
            input: "select",
            inputOptions: seasonOptions.reduce((obj, season) => {
              obj[season] = season; // 將選項轉換為鍵值對格式
              return obj;
            }, {}),
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newSeason) {
            try {
              await axios.patch(
                `/api/products/${productId}/season`,
                newSeason,
                {
                  headers: { "Content-Type": "text/plain" },
                }
              );
              Swal.fire("成功!", "季節已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "季節更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".edit-style-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const styleOptions = ["日系", "韓系", "美系", "歐系", "其他"];
          const { value: newStyle } = await Swal.fire({
            title: "更改風格",
            input: "select",
            inputOptions: styleOptions.reduce((obj, style) => {
              obj[style] = style; // 將選項轉換為鍵值對格式
              return obj;
            }, {}),
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newStyle) {
            try {
              await axios.patch(`/api/products/${productId}/style`, newStyle, {
                headers: { "Content-Type": "text/plain" },
              });
              Swal.fire("成功!", "風格已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "風格更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".edit-price-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const { value: newPrice } = await Swal.fire({
            title: "更改價格",
            input: "number",
            inputPlaceholder: "輸入新的價格",
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
          });
          if (newPrice) {
            try {
              const parsedPrice = parseFloat(newPrice);
              await axios.patch(
                `/api/products/${productId}/price`,
                parsedPrice,
                {
                  headers: { "Content-Type": "application/json" },
                }
              );
              Swal.fire("成功!", "價格已更新!", "success");
              this.refreshTable(); // 使用刷新方法
            } catch (error) {
              Swal.fire("錯誤!", "價格更新失敗!", "error");
            }
          }
        });
      document
        .querySelector(".insert-detail-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const { value: formValues } = await Swal.fire({
            title: "新增詳細資料",
            html: `
          <label for="new-size">尺寸：</label>
          <select id="new-size" class="swal2-input">
            <option value="XS">XS</option>
            <option value="S">S</option>
            <option value="M">M</option>
            <option value="L">L</option>
            <option value="XL">XL</option>
            <option value="XXL">XXL</option>
          </select>
          <label for="new-color">顏色：</label>
          <input type="text" id="new-color" class="swal2-input" placeholder="輸入顏色">
          <label for="new-sum">數量：</label>
          <input type="number" id="new-sum" class="swal2-input" placeholder="輸入數量">
        `,
            focusConfirm: false,
            showCancelButton: true,
            confirmButtonText: "保存",
            cancelButtonText: "取消",
            preConfirm: () => {
              const newSize = document.getElementById("new-size").value;
              const newColor = document.getElementById("new-color").value;
              const newSum = document.getElementById("new-sum").value;

              if (!newSize || !newColor || !newSum) {
                Swal.showValidationMessage("所有欄位都必須填寫！");
                return false;
              }

              return {
                size: newSize,
                color: newColor,
                sum: parseInt(newSum, 10),
              };
            },
          });

          if (formValues) {
            try {
              await axios.post(
                `/api/products/${productId}/details`,
                formValues,
                {
                  headers: { "Content-Type": "application/json" },
                }
              );

              Swal.fire("成功！", "詳細資料已新增！", "success");

              // 刷新子表數據
              const subTable = this.subTables[productId];
              if (subTable) {
                const response = await axios.get(
                  `/api/products/${productId}/details`
                );
                subTable.clear();
                subTable.rows.add(response.data);
                subTable.draw();
              }
            } catch (error) {
              Swal.fire("錯誤！", "新增詳細資料失敗！", "error");
            }
          }
        });
      document
        .querySelector(".delete-btn")
        .addEventListener("click", async (e) => {
          const id = e.target.dataset.id;
          const result = await Swal.fire({
            title: "確定刪除?",
            text: "此操作無法還原",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "是，刪除!",
            cancelButtonText: "取消",
          });
          if (result.isConfirmed) {
            try {
              await axios.delete(`/api/products/${id}`);
              Swal.fire("成功!", "商品已刪除!", "success");
              this.refreshTable(); // 刷新表格數據
            } catch (error) {
              Swal.fire("錯誤!", "刪除商品失敗!", "error");
            }
          }
        });
    },
    // 綁定父表事件
    bindMainTableEvents() {
      // 展開/收起子表邏輯
      $("#productTable tbody").on("click", "td.dt-control", async (event) => {
        const tr = $(event.currentTarget).closest("tr");
        const row = this.table.row(tr);

        if (row.child.isShown()) {
          // 收起子表
          row.child.hide();
          tr.removeClass("shown");
        } else {
          // 展開子表
          const productId = row.data().id;
          row.child(this.formatSubTable(productId)).show();
          tr.addClass("shown");

          // 初始化子表
          await this.loadSubTableData(productId);
        }
      });
    },

    // 格式化子表
    formatSubTable(productId) {
      return `<table id="detailTable-${productId}" class="display" style="width:100%">
        <thead>
          <tr>
            <th>細節ID</th>
            <th>尺寸</th>
            <th>顏色</th>
            <th>數量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>`;
    },

    // 加載子表數據並初始化 DataTable
    async loadSubTableData(productId) {
      try {
        const response = await axios.get(`/api/products/${productId}/details`);
        const subTable = $(`#detailTable-${productId}`).DataTable({
          data: response.data,
          columns: [
            { data: "detailId" },
            { data: "size" },
            { data: "color" },
            { data: "sum" },
            {
              data: null,
              render: (data) => `
              <button class="pinkbtn edited-btn" data-detailid="${data.detailId}">編輯</button>
                
              `,
            },
          ],
          paging: false,
          searching: false,
          info: false,
        });

        // 保存子表實例
        this.subTables[productId] = subTable;

        // 綁定子表操作邏輯
        this.bindSubTableEvents(subTable, productId);
      } catch (error) {
        Swal.fire("錯誤", "加載子表數據失敗", "error");
      }
    },

    // 綁定子表操作邏輯
    bindSubTableEvents(subTable, productId) {
      subTable.on("click", ".edited-btn", async (event) => {
        const detailId = $(event.currentTarget).data("detailid");
        Swal.fire({
          title: "選擇操作",
          html: `
        <button class="pinkbtn edit-size-btn" data-detailid="${detailId}">更改尺寸</button>
        <button class="pinkbtn edit-color-btn" data-detailid="${detailId}">更改顏色</button>
        <button class="pinkbtn edit-sum-btn" data-detailid="${detailId}">更改數量</button>
        <button class="pinkbtn delete-detail-btn" data-detailid="${detailId}">刪除</button>
      `,
          showCancelButton: true,
          showConfirmButton: false,
          cancelButtonText: "取消",
          didRender: () => {
            document
              .querySelector(".edit-size-btn")
              .addEventListener("click", async () => {
                const sizeOptions = ["XS", "S", "M", "L", "XL", "XXL"];
                const { value: newSize } = await Swal.fire({
                  title: "更改尺寸",
                  input: "select",
                  inputOptions: sizeOptions.reduce((obj, size) => {
                    obj[size] = size;
                    return obj;
                  }, {}),
                  showCancelButton: true,
                  confirmButtonText: "保存",
                  cancelButtonText: "取消",
                });

                if (newSize) {
                  try {
                    await axios.patch(
                      `/api/products/${productId}/details/${detailId}/size`,
                      newSize,
                      {
                        headers: { "Content-Type": "text/plain" },
                      }
                    );
                    Swal.fire("成功!", "尺寸已更新!", "success");
                    // 刷新子表
                    const response = await axios.get(
                      `/api/products/${productId}/details`
                    );
                    subTable.clear();
                    subTable.rows.add(response.data);
                    subTable.draw();
                  } catch (error) {
                    Swal.fire("錯誤!", "尺寸更新失敗!", "error");
                  }
                }
              });
            document
              .querySelector(".edit-color-btn")
              .addEventListener("click", async () => {
                const { value: newColor } = await Swal.fire({
                  title: "更改顏色",
                  input: "text",
                  showCancelButton: true,
                  confirmButtonText: "保存",
                  cancelButtonText: "取消",
                });

                if (newColor) {
                  try {
                    await axios.patch(
                      `/api/products/${productId}/details/${detailId}/color`,
                      newColor,
                      {
                        headers: { "Content-Type": "text/plain" },
                      }
                    );
                    Swal.fire("成功!", "顏色已更新!", "success");
                    // 刷新子表
                    const response = await axios.get(
                      `/api/products/${productId}/details`
                    );
                    subTable.clear();
                    subTable.rows.add(response.data);
                    subTable.draw();
                  } catch (error) {
                    Swal.fire("錯誤!", "顏色更新失敗!", "error");
                  }
                }
              });
            document
              .querySelector(".edit-sum-btn")
              .addEventListener("click", async () => {
                const { value: newSum } = await Swal.fire({
                  title: "更改數量",
                  input: "number",
                  showCancelButton: true,
                  confirmButtonText: "保存",
                  cancelButtonText: "取消",
                });

                if (newSum) {
                  try {
                    await axios.patch(
                      `/api/products/${productId}/details/${detailId}/sum`,
                      newSum,
                      {
                        headers: { "Content-Type": "application/json" },
                      }
                    );
                    Swal.fire("成功!", "數量已更新!", "success");
                    // 刷新子表
                    const response = await axios.get(
                      `/api/products/${productId}/details`
                    );
                    subTable.clear();
                    subTable.rows.add(response.data);
                    subTable.draw();
                  } catch (error) {
                    Swal.fire("錯誤!", "數量更新失敗!", "error");
                  }
                }
              });
            // 綁定刪除按鈕事件
            document
              .querySelector(".delete-detail-btn")
              .addEventListener("click", async () => {
                const result = await Swal.fire({
                  title: "確定刪除?",
                  text: "此操作無法還原",
                  icon: "warning",
                  showCancelButton: true,
                  confirmButtonText: "是，刪除!",
                  cancelButtonText: "取消",
                });

                if (result.isConfirmed) {
                  try {
                    await axios.delete(
                      `/api/products/${productId}/details/${detailId}`
                    );
                    Swal.fire("成功!", "細節已刪除.", "success");

                    // 更新子表數據
                    const response = await axios.get(
                      `/api/products/${productId}/details`
                    );
                    subTable.clear();
                    subTable.rows.add(response.data);
                    subTable.draw();
                  } catch (error) {
                    Swal.fire("錯誤!", "刪除失敗!", "error");
                  }
                }
              });
          },
        });
      });
    },
  },
};
</script>

<style scoped>
.floating-buttons {
  position: absolute;
  z-index: 1000;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  padding: 10px;
}

.floating-buttons button {
  margin: 5px 0;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  display: block;
}

.pinkbtn {
  background-color: #ffc107;
  color: black;
}

.pinkbtn:hover {
  background-color: #e0a800;
}

.floating-btn {
  position: relative;
  z-index: 2;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 16px;
  cursor: pointer;
}

.floating-btn:hover {
  background-color: #0056b3;
}
.action-buttons {
  margin-bottom: 20px;
}
.action-buttons1 {
  margin-bottom: 10px;
}

button {
  margin-right: 10px;
}

#addProductBtn {
  position: fixed;
  bottom: 20px;
  left: 790px;
  z-index: 1000;
  background-color: #c48888;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
}

#morePowerBtn {
  position: fixed;
  bottom: 20px;
  left: 930px;
  z-index: 1000;
  background-color: #c48888;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
}

#searchProductBtn {
  position: fixed;
  bottom: 20px;
  left: 840px;
  z-index: 1000;
  background-color: #c48888;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
}

#addProductBtn:hover {
  background-color: #804040;
}

#searchProductBtn:hover {
  background-color: #804040;
}

.delete-btn {
  color: aliceblue;
  background-color: #c48888;
  border: none;
  border-radius: 5px;
}
button:hover {
  background-color: #804040;
}

tr:hover td:hover {
  background-color: #ebd6d6;
}
.product-image {
  width: 30px; /* 設定寬度 */
  height: 30px; /* 設定高度 */
  object-fit: cover; /* 保持圖片比例 */
  border-radius: 5px; /* 圓角 */
}
</style>
