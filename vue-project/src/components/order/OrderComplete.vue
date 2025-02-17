<script setup>
import FooterView from '@/views/FooterView.vue';
import HeadLogin from '@/views/HeadLogin.vue';
import step3Checkout from './stepcheckout/step3Checkout.vue';

</script>

  
<template>   

  <div>
    <HeadLogin></HeadLogin>
  </div>

  <div style="margin:50px;"><step3Checkout></step3Checkout></div>

    <div class="border-cart">
        <div class="bi bi-arrow-right-circle-fill" > THANK YOU ! 完成訂購</div>
            <hr style="border: none; border-top: 1px solid black; margin: 10px 0;">

        <div class="order-complete" style="margin: 50px;">
            <h2>感謝您的購買！</h2>
            <h6>您的訂單已成功建立，我們會盡快處理。</h6>
        </div>

    </div>

     <div class="text-center mt-3" style="padding: 50px;">
        <router-link to="loginHome">
            <button class="btn btn-outline-secondary" 
            style="margin: 0 5px;">繼續購物
            </button>
        </router-link>
        <router-link to="memberOrder">
            <button class="btn btn-outline-secondary" 
            id="checkout-icon">查看訂單
            </button>
        </router-link>
    </div>

        <FooterView></FooterView>
</template>

<style scoped>
/* 整個購物車框框 */
.border-cart{
  border: 1px solid #e4e4e4;
  border-radius: 10px;
  padding: 25px;
  width: 85%; 
  margin: 0 auto;
    
}

.order-complete{
    display: flex;
    align-items: center;
    flex-direction: column;
}

.order-complete h1 {
    margin-bottom: 10px; 
    line-height: 1.2; 
    font-weight: bold;
    color: #333;
}

.order-complete h6 {
    margin-top: 0; 
    line-height: 1.2; 
    color: #666;
}

#checkout-icon{
  color: #C48888;
  border-color: #C48888;
}
#checkout-icon:hover{
  background-color: #C48888;
  color: white;
}
</style>
