<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>所有訂單資料</title>

<link rel="stylesheet" href="/css/order_style.css">
<link rel="stylesheet" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">

</head>

<style>
#container {
 	margin: 40px auto;
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h3{
	text-align: center;
}

th{
	border: 5px solid white;
	border-radius: 8px;
	margin: 20px 20px 20px;
	text-align: center;
  
}

th:hover{
	 background-color:#C48888;
	
}

#tr:hover{
	background-color:#C48888;

}

form input[type="button"] {
    width: 90%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    
}



form input[type="button"]:hover {
    background-color: #ebd6d6;
}

#paginate_button current:hover {
   
    background-color: #ebd6d6;
    cursor: pointer;
}

.dataTables_paginate .paginate_button:hover {
	background: none;
 	color: black
}



dialog {
  position: right;
  
  margin: auto;
}

.ui-dialog .ui-dialog-buttonpane button:hover {
	background-color: #ebd6d6;
}

#buttonOfInsertOrder:hover{
	background-color: #ebd6d6;
}

</style>



<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
 <script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
 <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.js"></script>



<body>
<%@ include file="../../index.jsp" %>
<header><h2>所有訂單資料</h2></header>

<br>
<button id="buttonOfInsertOrder">新增訂單</button>

<div id="insertOrderDialog" title="新增訂單">
        <form method="post" action="${pageContext.request.contextPath}/insert" name="formOfInsertOrderDialog">
            <label>輸入訂單ID：</label>
            <input type="text" name="orderId" required><br>

            <label>輸入用戶ID：</label>
            <input type="text" name="userId" required><br>

            <label>輸入商品總金額：</label>
            <input type="text" name="merchandiseSubtotal" required><br>

            <label>輸入運費：</label>
            <input type="text" name="shippingFee"><br>

            <label>輸入優惠券ID：</label>
            <input type="text" name="couponId"><br>

            <label>輸入折扣後金額：</label>
            <input type="text" name="orderTotal" required><br>

            <label>選擇付款方式：</label>
            <select id="paymentMethod" name="paymentMethod">
                <option>信用卡</option>
                <option>銀行轉帳</option>
                <option>貨到付款</option>
            </select><br>

            <label>選擇寄送方式：</label>
            <select id="shippingOption" name="shippingOption">
                <option>宅配</option>
                <option>7-ELEVEN</option>
                <option>全家</option>
            </select><br>

            <label>選擇訂單狀態：</label>
            <select id="status" name="status">
                <option>待付款</option>
                <option>待出貨</option>
                <option>待收貨</option>
                <option>退貨/退款</option>
                <option>訂單已完成</option>
            </select><br>

            <label>輸入下單時間：</label>
            <input type="text" name="orderTime" placeholder="yyyy-MM-dd HH:mm:ss"><br>

            <label>輸入出貨時間：</label>
            <input type="text" name="shippingTime" placeholder="yyyy-MM-dd HH:mm:ss"><br>

            <label>輸入配送完成時間：</label>
            <input type="text" name="deliveryCompletionTime" placeholder="yyyy-MM-dd HH:mm:ss"><br>
        </form>
    </div>

<div id="container">
<table id="table">

<thead>
<tr>
<th>ID</th>
<th>訂單ID</th>
<th>用戶ID</th>
<th>商品總金額</th>
<th>運費</th>
<th>優惠券ID</th>
<th>折扣後金額</th>
<th>付款方式</th>
<th>寄送方式</th>
<th>訂單狀態</th>
<th>下單時間</th>
<th>出貨時間</th>
<th>配送完成時間</th>
<th>動作</th>
</tr>
</thead>

 <tbody>
 
  <!-- 使用 JSTL 的 c:forEach 來遍歷 orders -->
   <c:forEach var="order" items="${orders}">
    
	<tr id="tr">	
	<td>${order.id}</td>
	<td>${order.orderId}</td>
	<td>${order.userId}</td>
	<td>${order.merchandiseSubtotal}</td>
	<td>${order.shippingFee}</td>
	<td>${order.couponId}</td>
	<td>${order.orderTotal}</td>
	<td>${order.paymentMethod}</td>
	<td>${order.shippingOption}</td>
	
	<td>
	<select name="chooseStatus">
		<option value="待付款" ${"待付款".equals(order.status) ? "selected" : ""}>待付款</option>
   		<option value="待出貨" ${"待出貨".equals(order.status) ? "selected" : ""}>待出貨</option>
   		<option value="待收貨" ${"待收貨".equals(order.status) ? "selected" : ""}>待收貨</option>
    	<option value="退貨/退款" ${"退貨/退款".equals(order.status) ? "selected" : ""}>退貨/退款</option>
    	<option value="訂單已完成" ${"訂單已完成".equals(order.status) ? "selected" : ""}>訂單已完成</option>
  </select>
	</td>
	
	<td>${order.orderTime}</td>
	<td>${order.shippingTime}</td>
	<td>${order.deliveryCompletionTime}</td>
	
	<td>
	<form method="post" name="form" >
		<input type="hidden" id="id" name="id" value="${order.id}" />
		<input type="hidden" name="status" id="hiddenStatus" />
		<input type="button" class="update" value="修改" />	
		<input type="button" class="delete" value="刪除" />		
	</form>
	</td>
	</tr>
		
 </c:forEach>

</tbody>
</table>
</div>


	
<script>

$(document).ready(function () {
	
	$( "#buttonOfInsertOrder" )
    .button()
    .click(function() {
      $( "#insertOrderDialog" ).dialog( "open" );
    	});
	
	$( "#insertOrderDialog" ).dialog({
	    autoOpen: false,
	    maxWidth: 800, 
	    width: "50%",  
	    height: "auto" ,
	    modal: true,
	    buttons: {
	        "確定新增": function() {
	        	$("form[name='formOfInsertOrderDialog']").attr("action","insert");
	    		$("form[name='formOfInsertOrderDialog']").submit();	
	            Swal.fire({
	      		  position: "center",
	      		  icon: "success",
	      		  title: "訂單新增成功",
	      		  showConfirmButton: false,
	      		  timer: 2000
	      		  
	      		});
	            $( this ).dialog( "close" );
	        },
	        "取消": function() {
	            $( this ).dialog( "close" );
	        }
	    }
	});
	         
	$(".update").on("click", function() {
	    let selectStatus = $(this).closest("tr").find("select[name='chooseStatus']").val();
	    $(this).closest("form").find("input[name='status']").val(selectStatus);
	    $(this).closest("form").attr("action", "update"); 
	    $(this).closest("form").submit(); 
	});
	
	$(".delete").on("click" , function(){
		$(this).closest("form").attr("action","delete");
		$(this).closest("form").submit();
	})
	
     if ("${isDeleted}" === "true") {
        Swal.fire({
            position: "center",
            icon: "success",
            title: "訂單刪除成功",
            showConfirmButton: false,
            timer: 1500
        });
    }
    
     if ("${update}" === "true") {
        Swal.fire({
            position: "center",
            icon: "success",
            title: "訂單修改成功",
            showConfirmButton: false,
            timer: 1500
        });
    }
	
	$('#table').DataTable(); 

	
	
	
});



</script>
</body>

</html>