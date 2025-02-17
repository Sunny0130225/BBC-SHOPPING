# BBC 購物網站 (BuyBuyClothes)

## 專案介紹
BBC 購物網站 (BuyBuyClothes) 是一個簡單、人性化的線上購物平台，主要提供服飾類商品的瀏覽與購買。用戶可以快速熟悉商城介面，輕鬆完成購物流程。此外，我們也開發了完整的後台管理系統，讓管理者能夠高效處理訂單、管理商品及分析銷售數據。

---

## 技術棧 (Tech Stack)
### **前端**
- Vue.js
- Vuetify
- SweetAlert
- jQuery
- ajax/axios
- HTML / CSS / JavaScript

### **後端**
- Java (Spring Boot / Spring MVC)
- JPA (Java Persistence API)
- SQL Server
- Tomcat

### **其他**
- Git 版本控制
- Node.js (前端環境)
- npm (套件管理)

---

## 主要功能
### **前台功能**
商品瀏覽  
註冊 / 登入 / 登出  
會員可加入購物車並結帳  
訪客無法加入購物車，將跳轉至登入頁面  

### **後台功能**
 會員管理 (權限分配)  
 商品管理 (新增 / 編輯 / 刪除)  
 訂單管理  
 折扣管理  
 僅 **管理者 (admin) & 最高管理者 (superadmin)** 可登入後台  

---

##  安裝與執行
### **1. 安裝後端 (Spring Boot)**
1. 確保已安裝 **Java 8+** 和 **SQL Server**
2. 進入 `team3` 目錄，找到 `src\main\java\com\bbc31Application.java`
3. 執行 Spring Boot 專案

### **2. 安裝前端 (BBC 後台)**
1. 打開 CMD，進入 @latest-2
2. 執行：
   npm install
   npm run dev
### **3. 安裝前端 (BBC 前台)**
1. 打開 CMD，進入 vue-project
2. 執行：
   npm install
   npm run dev
### **4. 新增登入資料**
1. 新增一個membership
2. 為此membership新增memberdetail的role=admin/superadmin
3. 要有一個會員是admin/superadmin才可以登入bbc的後台
