<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>幸運轉盤管理</title>
    <link rel="stylesheet" type="text/css" href="/css/discount_style.css">
    <script>
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

        function closeEditDialog() {
            document.getElementById("editDialog").style.display = "none";
            document.getElementById("overlay").style.display = "none";
        }
    </script>
    <style>
        .dialog {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: white;
            padding: 20px;
            border: 1px solid #ccc;
            z-index: 1000;
        }

        #overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
    </style>
</head>
<body>
<%@ include file="../../index.jsp" %>
    <h2>幸運轉盤管理</h2>

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
            <c:forEach var="discount" items="${specialDiscounts}">
                <tr>
                    <td>${discount.dname}</td>
                    <td>${discount.dtype}</td>
                    <td>${discount.dpercent}</td>
                    <td>${discount.ddepart}</td>
                    <td>${fn:replace(discount.dts, 'T', '<br>')}</td>
                    <td>${fn:replace(discount.dte, 'T', '<br>')}</td>
                    <td>
                        <button type="button" class="edit-button" 
                                onclick="openEditDialog('${discount.did}', '${discount.dname}', '${discount.dtype}', '${discount.dpercent}', '${discount.ddepart}', '${discount.dts}', '${discount.dte}')">
                            編輯
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- 遮罩層 -->
    <div id="overlay" onclick="closeEditDialog();"></div>

    <!-- 編輯彈窗 -->
    <div id="editDialog" class="dialog">
        <h3>編輯折扣</h3>
        <form method="post" action="/applyDiscount2/save">
            <input type="hidden" name="did" id="editDid">

            <label for="editDname">折扣名稱：</label>
            <input type="text" name="dname" id="editDname" required><br>
            
            <label for="editDtype">折扣類型：</label>
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
