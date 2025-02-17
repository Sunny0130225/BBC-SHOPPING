<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>新增刪除</title>
    <link rel="stylesheet" type="text/css" href="/css/discount_style.css">
    <script>
        // 打开编辑弹窗并填充数据
        function openEditDialog(did, dname, dtype, dpercent, ddepart, dts, dte) {
            document.getElementById("editDid").value = did;
            document.getElementById("editDname").value = dname;
            document.getElementById("editDtype").value = dtype;
            document.getElementById("editDpercent").value = dpercent;
            document.getElementById("editDdepart").value = ddepart;
            document.getElementById("editDts").value = dts;
            document.getElementById("editDte").value = dte;
            document.getElementById("editDialog").style.display = "block";
            document.getElementById("overlay").style.display = "block";
        }

        // 关闭编辑弹窗
        function closeEditDialog() {
            document.getElementById("editDialog").style.display = "none";
            document.getElementById("overlay").style.display = "none";
        }

        // 打开新增弹窗
        function openAddDialog() {
            document.getElementById("addDialog").style.display = "block";
            document.getElementById("overlay").style.display = "block";
        }

        // 关闭新增弹窗
        function closeAddDialog() {
            document.getElementById("addDialog").style.display = "none";
            document.getElementById("overlay").style.display = "none";
        }
    </script>
    <style>
        /* 弹窗样式 */
        .dialog {
            display: none; /* 默认隐藏 */
            position: fixed; /* 固定位置 */
            top: 50%; /* 垂直居中 */
            left: 50%; /* 水平居中 */
            transform: translate(-50%, -50%); /* 确保完全居中 */
            background: white;
            padding: 20px;
            border: 1px solid #ccc;
            z-index: 1000; /* 确保弹窗位于最前面 */
        }

        /* 遮罩层样式 */
        #overlay {
            display: none; /* 默认隐藏 */
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
            z-index: 999; /* 确保遮罩层位于弹窗下方 */
        }
    </style>
</head>
<body>
<%@ include file="../../index.jsp" %>
    <h1>新增刪除</h1>

    <!-- 打开新增弹窗按钮 -->
    <button type="button" onclick="openAddDialog()">新增折扣</button>

    <!-- 快速刪除按鈕 -->
    <form action="/discounts/deleteExpired" method="post" class="other-form">
        <button type="submit" class="delete-all-button">快速刪除過期折扣</button>
    </form>

	<form action="/discounts/insertExpired" method="post" class="other-form">
    	<button type="submit" class="add-expired-button">快速新增過期折扣</button>
	</form>

    <h2>現有折扣</h2>

    <table border="1">
        <thead>
            <tr>
                <th>折扣名稱</th>
                <th>折扣類型</th>
                <th>折扣</th>
                <th>限制</th>
                <th>開始時間</th>
                <th>結束時間</th>
                <th>編輯</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty discounts}">
                    <c:forEach var="discount" items="${discounts}">
                        <tr>
                            <td>${discount.dname}</td>
                            <td>${discount.dtype}</td>
                            <td>${discount.dpercent}</td>
                            <td>${discount.ddepart}</td>
                            <td>${fn:replace(discount.dts, 'T', '<br>')}</td>
                            <td>${fn:replace(discount.dte, 'T', '<br>')}</td>
                            <td>
                                <button type="button" class="edit-button" 
                                        onclick="openEditDialog('${discount.did}', '${discount.dname}', '${discount.dtype}', '${discount.dpercent}', '${discount.ddepart}', '${discount.dts}', '${discount.dte}')">編輯</button>
                                <form action="/discounts/delete/${discount.did}" method="post" class="deldtd-form">
                                    <button type="submit" class="delete-button">刪除</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6">目前沒有任何折扣資料</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <!-- 遮罩层 -->
    <div id="overlay" onclick="closeEditDialog(); closeAddDialog();"></div>

    <!-- 新增弹窗 -->
    <div id="addDialog" class="dialog">
        <h3>新增折扣</h3>
        <form method="post" action="/discounts/save">
            <label for="addDname">折扣名稱：</label>
            <input type="text" name="Dname" id="addDname" required><br>
			
			<label for="addDtype">限制：</label>
            <select name="Dtype" id="addDtype" required>
                <option value="現金">現金</option>
                <option value="百分比">百分比</option>
            </select><br>
			
            <label for="addDpercent">折扣百分比：</label>
            <input type="number" name="Dpercent" id="addDpercent" min="0" max="100" required><br>

            <label for="addDdepart">限制：</label>
            <select name="Ddepart" id="addDdepart" required>
                <option value="無限制">無限制</option>
                <option value="男裝">男裝</option>
                <option value="女裝">女裝</option>
                <option value="童裝">童裝</option>
                <option value="中性">中性</option>
            </select><br>

            <label for="addDts">開始時間：</label>
            <input type="datetime-local" name="Dts" id="addDts" required><br>

            <label for="addDte">結束時間：</label>
            <input type="datetime-local" name="Dte" id="addDte" required><br>

            <button type="submit">新增</button>
            <button type="button" onclick="closeAddDialog()">关闭</button>
        </form>
    </div>

    <!-- 编辑弹窗 -->
    <div id="editDialog" class="dialog">
        <h3>编辑折扣</h3>
        <form method="post" action="/discounts/save">
            <input type="hidden" name="did" id="editDid">

            <label for="editDname">折扣名稱：</label>
            <input type="text" name="dname" id="editDname" required><br>
			
			<label for="editDdepart">限制：</label>
            <select name="dtype" id="editDtype" required>
                <option value="現金">現金</option>
                <option value="百分比">百分比</option>
            </select><br>
			
            <label for="editDpercent">折扣百分比：</label>
            <input type="number" name="dpercent" id="editDpercent" min="0" max="100" required><br>

            <label for="editDdepart">限制：</label>
            <select name="ddepart" id="editDdepart" required>
                <option value="無限制">無限制</option>
                <option value="男裝">男裝</option>
                <option value="女裝">女裝</option>
                <option value="童裝">童裝</option>
                <option value="中性">中性</option>
            </select><br>

            <label for="editDts">開始時間：</label>
            <input type="datetime-local" name="dts" id="editDts" required><br>

            <label for="editDte">結束時間：</label>
            <input type="datetime-local" name="dte" id="editDte" required><br>

            <button type="submit">更新</button>
            <button type="button" onclick="closeEditDialog()">关闭</button>
        </form>
    </div>

</body>
</html>
