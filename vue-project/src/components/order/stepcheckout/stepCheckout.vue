<script setup>
    
</script>

<template>
    <div class="step-container">
        <div class="step active" style="background-color:#e8caca">
            <div class="bi bi-check-lg" id="icon"></div>
            <div class="text">確認購買明細<br><span>CHECK YOUR ORDER</span></div>
        </div>
        <div class="step">
            <div class="bi bi-truck" id="icon"></div>
            <div class="text">配送與付款方式<br><span>SHIPPING & PAYMENT</span></div>
        </div>
        <div class="step">
            <div class="bi bi-pencil" id="icon"></div>
            <div class="text">填寫收件資料<br><span>DELIVERY INFORMATION</span></div>
        </div>
        <div class="step">
            <div class="bi bi-box2-heart" id="icon"></div>
            <div class="text">完成訂購<br><span>THANK YOU!</span></div>
        </div>
    </div>

</template>

<style lang="css" scoped>
.step-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.step {
  flex: 1;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
  background-color: #f9f9f9;
  margin: 0 5px;
  cursor: pointer;
}

.step.active {
  background-color: #e0e0e0;
  border-color: #666;
}

.step #icon {
  font-size: 20px;
  margin-bottom: 10px;
}

.step .text {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.step .text span {
  font-size: 12px;
  color: #666;
}

</style>