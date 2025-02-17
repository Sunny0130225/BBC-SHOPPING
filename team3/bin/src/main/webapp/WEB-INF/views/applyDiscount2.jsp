<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/discount_style.css">
    <title>Discount Application</title>
</head>
<body>
	<%@ include file="../../index.jsp" %>
    <h1>Discount示範</h1>

    <form action="/calculate" method="get">
        <label for="productId">選擇商品</label>
        <select name="productId" id="productId" required>
        <option value="">-- 未選擇 --</option>
            <c:forEach var="product" items="${productItems}">
                <option value="${product.id}" 
                    <c:if test="${product.id == selectedProductId}">selected</c:if>>
                    ${product.name}　(${product.depart})
                </option>
            </c:forEach>
        </select>

        <label for="discountId">選擇折扣</label>
        <select name="discountId" id="discountId" required>
        <option value="">-- 未選擇 --</option>
            <c:forEach var="discount" items="${discounts}">
            	<c:if test="${discount.dte >= now}">
                	<option value="${discount.did}" 
                    	<c:if test="${discount.did == selectedDiscountId}">selected</c:if>>
                   		${discount.dname}　(${discount.dpercent}<c:choose>
													               <c:when test="${discount.dtype == '百分比'}">%</c:when>
													               <c:when test="${discount.dtype == '現金'}">元</c:when>			                
													           </c:choose>)
                	</option>
                </c:if>
            </c:forEach>
        </select>

        <button type="submit">確認</button>
    </form>

    <c:if test="${not empty originalPrice}">
    	<h2>商品名: ${showName}</h2>
    	<h2>${showDepart}</h2>
        <h2>原價格: ${originalPrice}</h2>
        <h2>折扣價: ${discountedPrice}</h2>
    </c:if>

    <c:if test="${not empty error}">
        <p style="color: red; text-align: center;">${error}</p>
    </c:if>
</body>
</html>
