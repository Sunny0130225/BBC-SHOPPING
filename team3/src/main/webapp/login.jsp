<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Application</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
 
<!-- css related code which we can have either in 
     same jsp or separately also in a css file -->
<style>
body {font-family: Arial, Helvetica, sans-serif;
display:flex;
justify-content:center;
align-items:center;
}
form {border: 3px solid #f1f1f1;}
 
input[type=text], input[type=password] {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  box-sizing: border-box;
}
 
button {
  background-color:  #C48888;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  border-radius: 5px
  cursor: pointer;
  text-align: center;
  width: 100%;
}
 
button:hover {
  background-color: #804040;
transform: scale(1.05);
}
 
.cancelbutton {
  width: auto;
  padding: 10px 18px;
  background-color: #f44336;
}
 
.container {
  padding: 16px;
}
 
span.psw {
  float: right;
  padding-top: 16px;
}
 
/* Change styles for span and cancel button
   on extra small screens */
@media screen and (max-width: 300px) {
  span.psw {
     display: block;
     float: none;
  }
  .cancelbutton {
     width: 100%;
  }
}
</style>
</head>
<body>
 
    <form method="post" action="/member/login" onsubmit="return validateForm()">
         <div class="container">
    <label for="email"><b>電子郵件</b></label>
    <input type="text" placeholder="請輸入電子郵件" name="email" id = "email" required>
 
    <label for="password"><b>密碼</b></label>
    <input type="password" placeholder="請輸入密碼" name="password" id="password" required>
         
    <button type="submit">登入</button>
   
    <label>
      <input type="checkbox" name="rememberme" id="rememberme"> 記住我
    </label>
  </div>
 <% 
    String errorMessage = (String) session.getAttribute("error");
    if (errorMessage != null) {
%>
    <div style="color: red; text-align: center; margin-top: 10px;">
        <%= errorMessage %>
    </div>
<%
        session.removeAttribute("error"); // 顯示後移除錯誤訊息
    }
%>
  <!-- <div class="container" style="background-color:#f1f1f1">
    <button type="button" class="cancelbutton">取消</button>
    <span class="psw">忘記 <a href="forgotpassword.jsp">密碼?</a></span>
  </div>-->
    </form>
    
    <script>
        // 驗證電子郵件格式
        
        function validateForm() {
            var email = document.getElementById("email").value;
            var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!emailPattern.test(email)) {
                alert("電子郵箱格式錯誤");
                return false;  // 阻止表單提交
            }
            
        //記住我 是把帳號和密碼存在用戶電腦上的cookie
          // 如果選中記住我，則保存信息到 cookies
         // 記住我功能：儲存帳號、密碼和記住我狀態
            if (document.getElementById("rememberme").checked) {
                setCookie("email", document.getElementById("email").value, 7);  // 記住用戶電子郵件7天
                setCookie("password", document.getElementById("password").value, 7);  // 記住密碼7天
                setCookie("rememberme", "true", 7);  // 記住勾選狀態7天
            } else {
                // 如果取消勾選「記住我」，清除相應的 cookies
                deleteCookie("email");
                deleteCookie("password");
                deleteCookie("rememberme");
            }

            return true;  // 正確格式，提交表單
        }

        // 設置 cookie
        function setCookie(name, value, days) {
            var d = new Date();
            d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000));  // 設置過期時間
            var expires = "expires=" + d.toUTCString();
            document.cookie = name + "=" + value + ";" + expires + ";path=/";
        }

        // 獲取 cookie
        function getCookie(name) {
            var name = name + "=";
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i].trim();
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }

        // 刪除 cookie
        function deleteCookie(name) {
            document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }

        // 頁面加載時檢查是否有「記住我」的 cookie
        window.onload = function () {
            var email = getCookie("email");
            var password = getCookie("password");
            var rememberMeChecked = getCookie("rememberme") === "true";

            // 如果有 email 且 rememberme 被勾選，則自動填充表單
            if (email != "") {
                document.getElementById("email").value = email;
            }
            if (password != "" && rememberMeChecked) {
                document.getElementById("password").value = password;
                document.getElementById("rememberme").checked = true;
            }
        };
        </script>

    
</body>
</html>