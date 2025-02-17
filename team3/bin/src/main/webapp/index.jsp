<%@ page pageEncoding="UTF-8" %>
<style>
  body {
    line-height: normal !important;
  }
  #function {
    width: 100vw;
    height: 80px;
    color: white;
    background-color: #4f4f4f;
    display: flex;
    justify-content: flex-end;
    padding-top: 13px;
    line-height: 1;
    margin: 0;
    padding: 0;
  }
  #login {
    width: 140px;
    height: 30px;
    padding-top: 2px;
  }
  #logout a {
    padding-top: 2px;
    padding-right: 20px;
    height: 30px;
    text-decoration: none;
    color: white;
  }
  #header {
    background-color: #c48888;
    color: white;
    text-align: center;
    padding: 20px;
    margin: 0;
    box-sizing: border-box;
    border-radius: 0;
    width: 100vw;
    height: 120px;
    display: flex;
    justify-content: center;
    align-items: center;
    h1 {
      font-weight: normal;
    }
  }
  #box {
    padding: 0;
    margin: 0;
    width: 100vw;
    height: 50px;
    background-color: #4f4f4f;
    overflow: hidden;
    display: flex;
    justify-content: space-evenly;
  }
  #botten {
    flex: 1;
    float: left;
    display: block;
    color: white;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0px 0px;
    margin: 0;
    text-decoration: none;
    font-size: 20px;
  }
  #botten:hover {
    background-color: #ddd;
    color: black;
  }
</style>
<first>
  <div id="function">
  </div>
</first>
<nav id="box">
  <a href="http://localhost:5173/index/memberView"" id="botten">會員管理</a>
  <a href="http://localhost:5173/index/productView"" id="botten">商品管理</a>
  <a href="http://localhost:5173/index/ordersView" id="botten">訂單管理</a>
  <a href="http://localhost:8081/discounts" id="botten">折扣管理</a>
</nav>
