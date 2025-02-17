<template>
  <div>
    <HeadLogin></HeadLogin>
    <div class="title-text">幸運輪盤</div>
    <div class="title-text2">折價券抽獎</div>
    <div class="lucky-draw">
      <div class="grid">
        <div
          v-for="(name, index) in gridSequence"
          :key="index"
          :class="['grid-item', { active: activeIndex === index }]"
          :style="getItemStyle(index)"
        >
          {{ name }}
        </div>
      </div>
      <button
        class="start-button"
        @click="startDraw"
        :disabled="isSpinning || remainingCount === 0"
      >
        {{ isSpinning ? "Spinning..." : "Start" }}
      </button>
    </div>
    <footer-view></footer-view>
  </div>
</template>

<script>
import axios from "axios";
import FooterView from "@/views/FooterView.vue";
import HeadLogin from "@/views/HeadLogin.vue";
export default {
  components: { FooterView, HeadLogin },
  data() {
    return {
      activeIndex: null, // 當前高亮的索引
      isSpinning: false, // 是否正在抽獎
      result: null, // 抽中的結果
      gridSequence: [], // 用於輪盤動畫的順序對應 LDname
      LDnames: [], // 從後端獲取的名稱
      remainingCount: 0, // 剩餘抽獎次數
    };
  },
  async mounted() {
    try {
      const response = await axios.get("/api/luckydraw");
      console.log("Lucky Draw Data:", response.data); // 確認是否獲取到資料
      this.LDnames = response.data.map((item) => item.ldname);

      // 按順時針排列 LDname，並將中間格子設為空
      this.gridSequence = [
        this.LDnames[0], // 左上
        this.LDnames[1], // 上中
        this.LDnames[2], // 右上
        this.LDnames[5], // 右中
        "", // 中間空白
        this.LDnames[4], // 右下
        this.LDnames[7], // 下中
        this.LDnames[6], // 左下
        this.LDnames[3], // 左中
      ];
      const memberId = sessionStorage.getItem("memberId");
      if (memberId) {
        const countResponse = await axios.get("/api/luckydraw/remainingCount", {
          params: { memberId },
        });
        this.remainingCount = countResponse.data;
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  },
  methods: {
    getItemStyle(index) {
      return {
        visibility: this.gridSequence[index] === "" ? "hidden" : "visible", // 隱藏中間的方塊
      };
    },

    startDraw() {
      if (this.isSpinning || !this.gridSequence.length) return;

      this.isSpinning = true;

      // 顺时针的有效索引
      const validIndexes = [0, 1, 2, 5, 8, 7, 6, 3];
      const resultIndex =
        validIndexes[Math.floor(Math.random() * validIndexes.length)];

      const totalSteps = 24 + validIndexes.indexOf(resultIndex); // 至少转两圈
      let step = 0;
      let currentIndex = 0;

      const interval = setInterval(() => {
        // 更新当前高亮的索引
        this.activeIndex = validIndexes[currentIndex];
        currentIndex = (currentIndex + 1) % validIndexes.length; // 顺时针更新索引
        step++;

        if (step > totalSteps) {
          clearInterval(interval);
          this.isSpinning = false;

          // 抽中後更新後端的 LDmax
          const selectedName = this.gridSequence[resultIndex];
          const selectedIndex = this.LDnames.findIndex(
            (name) => name === selectedName
          );

          if (selectedIndex !== -1) {
            const selectedId = selectedIndex + 1; // 假設 id 和索引是對應的
            const discountId = selectedId; // 折價券 id

            // 抽中後更新後端的 LDmax
            axios
              .post(`/api/luckydraw/decrease?id=${selectedId}`)
              .then(() => {
                console.log(
                  `LDmax for ${selectedName} decreased successfully.`
                );
              })
              .catch((error) => {
                console.error("Failed to update LDmax:", error);
              });

            // 儲存中獎資訊
            const memberId = sessionStorage.getItem("memberId");
            if (memberId) {
              axios
                .post(
                  "http://localhost:8081/api/luckydraw/saveWinningInfo",
                  null,
                  {
                    params: { memberId, discountId },
                  }
                )
                .then(() => {
                  console.log(
                    `Winning info saved successfully for memberId: ${memberId}, discountId: ${discountId}`
                  );
                  this.remainingCount--;
                })
                .catch((error) => {
                  console.error("Error saving winning info:", error);
                  alert("Failed to save winning info.");
                });
            } else {
              console.warn("Member ID is not available in session storage.");
            }
          }

          // 延迟 0.5 秒显示弹窗
          setTimeout(() => {
            this.$nextTick(() => {
              alert(`恭喜！你抽中了 "${this.gridSequence[resultIndex]}"！`);
            });
          }, 500);
        }
      }, 100); // 每次变化的时间间隔
    },
  },
};
</script>

<style scoped>
.title-text {
  text-align: center;
  margin-top: 20px;
  font-size: 50px;
  color: #ff0000;
}

.title-text2 {
  text-align: center;
  margin-top: 20px;
  font-size: 30px;
  color: #854444;
}

.lucky-draw {
  text-align: center;
  margin-top: 0px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 100px);
  grid-gap: 10px;
  justify-content: center;
  align-items: center;
}

.grid-item {
  width: 100px;
  height: 100px;
  line-height: 20px;
  background-color: #f0f0f0;
  border: 2px solid #ddd;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  border-radius: 8px;
  transition: background-color 0.3s;
  overflow: hidden; /* 隱藏溢出的文字 */
  text-overflow: ellipsis; /* 省略號表示溢出 */
  white-space: normal; /* 允許換行 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.grid-item.active {
  background-color: #ffcc00;
}

.start-button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  background-color: #c48888;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.start-button:disabled {
  background-color: #bbb;
  cursor: not-allowed;
}
</style>
