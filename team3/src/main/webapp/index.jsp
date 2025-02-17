<%@page import="com.bbc.membership.modal.Member"%>
<%@page import="com.bbc.membership.modal.MemberDetails"%>
<%@ page pageEncoding="UTF-8" %>
<style>
  body {
    line-height: normal !important;
  }
  #function {
    width: 100vw;
      height: 50px;
    color: white;
    background-color: #4f4f4f;
    display: flex;
    justify-content: space-between;
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
  .logout {
  background: none;      /* 移除背景色 */
    border: none;          /* 移除邊框 */      
    color: inherit;        /* 繼承父元素文字顏色 */
    font: inherit;         /* 繼承父元素字體樣式 */
    cursor: pointer;       /* 設置為可點擊的手型圖標 */
    padding:0px;
   margin:0px 15px 0px 0px;
    width:50px;
    height:32px;
  }
</style>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
function handleLogout(){
	axios.post("/member/logout").then((resp)=>{
		window.location.href="http://localhost:5173/"
	})
}
</script>
<first>
  <div id="function">
<span style="font-size: 30px; display: inline; padding: 10px"
>BBC管理後臺</span
> 
   <%Member member=(Member)session.getAttribute("member"); %>
  <%MemberDetails details=(MemberDetails)session.getAttribute("detail"); %>
  <span style="display: flex; justify-content: flex-end;padding-top: 5px">
  <span style="margin: 8px;">您好，<%=member.getUsername() %>(<%=details.getRole().equals("admin")?"管理者":"最高管理者" %>)</span>
  <!-- 登出按鈕 -->
        <button onclick="handleLogout()" class="logout">登出</button>
    </div>
    </span>
    <!--  
<span id="login">
Welcome, ${sessionScope.username}
</span>
<span id="logout">
<a href="http://localhost:8080/team3/membership/login/login.jsp">登出</a>
</span>
-->

 
  
</first>
<nav id="box">
  <a href="http://localhost:5173/index/memberView"" id="botten">會員管理</a>
  <a href="http://localhost:5173/index/productView"" id="botten">商品管理</a>
  <a href="http://localhost:5173/index/ordersView" id="botten">訂單管理</a>
  <a href="http://localhost:8081/discounts" id="botten">折扣管理</a>
  <a href="http://localhost:8081/applyDiscount2" id="botten">折扣應用</a>
</nav>
