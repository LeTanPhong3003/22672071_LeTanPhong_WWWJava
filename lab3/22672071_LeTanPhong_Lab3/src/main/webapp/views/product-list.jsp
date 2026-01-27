<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách sản phẩm</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            background-color: #ecf0f1;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
            text-align: center;
        }
        .header-actions {
            margin-bottom: 20px;
            text-align: right;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            transition: all 0.3s;
            font-size: 14px;
        }
        .btn-add {
            background-color: #27ae60;
            color: white;
        }
        .btn-add:hover {
            background-color: #229954;
        }
        .btn-edit {
            background-color: #f39c12;
            color: white;
            padding: 6px 12px;
            font-size: 13px;
        }
        .btn-edit:hover {
            background-color: #e67e22;
        }
        .btn-delete {
            background-color: #e74c3c;
            color: white;
            padding: 6px 12px;
            font-size: 13px;
        }
        .btn-delete:hover {
            background-color: #c0392b;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th {
            background-color: #34495e;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: bold;
        }
        td {
            padding: 12px;
            border-bottom: 1px solid #ecf0f1;
        }
        tr:hover {
            background-color: #f8f9fa;
        }
        tr:last-child td {
            border-bottom: none;
        }
        .message {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 4px;
            text-align: center;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .no-data {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
        }
        .price {
            color: #e74c3c;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Danh sách Sản phẩm</h1>

        <%
            String message = request.getParameter("message");
            String error = request.getParameter("error");

            if ("created".equals(message)) {
        %>
            <div class="message success">✓ Thêm sản phẩm thành công!</div>
        <%
            } else if ("updated".equals(message)) {
        %>
            <div class="message success">✓ Cập nhật sản phẩm thành công!</div>
        <%
            } else if ("deleted".equals(message)) {
        %>
            <div class="message success">✓ Xóa sản phẩm thành công!</div>
        <%
            } else if ("failed".equals(error)) {
        %>
            <div class="message error">✗ Thao tác thất bại!</div>
        <%
            }
        %>

        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/new" class="btn btn-add">+ Thêm sản phẩm mới</a>
        </div>

        <%
            List<Product> products = (List<Product>) request.getAttribute("products");
            if (products != null && !products.isEmpty()) {
        %>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá (VNĐ)</th>
                    <th>URL Hình ảnh</th>
                    <th style="text-align: center;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Product product : products) {
                %>
                <tr>
                    <td><%= product.getId() %></td>
                    <td><%= product.getName() %></td>
                    <td class="price"><%= String.format("%,.0f", product.getPrice()) %></td>
                    <td>
                        <% if (product.getUrlImage() != null && !product.getUrlImage().isEmpty()) { %>
                            <%= product.getUrlImage().substring(0, Math.min(50, product.getUrlImage().length())) %>...
                        <% } else { %>
                            <em style="color: #95a5a6;">Không có</em>
                        <% } %>
                    </td>
                    <td style="text-align: center;">
                        <a href="${pageContext.request.contextPath}/edit?id=<%= product.getId() %>" class="btn btn-edit">Sửa</a>
                        <a href="${pageContext.request.contextPath}/delete?id=<%= product.getId() %>"
                           class="btn btn-delete"
                           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">Xóa</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
            } else {
        %>
        <div class="no-data">
            <p>Chưa có sản phẩm nào. Hãy thêm sản phẩm mới!</p>
        </div>
        <%
            }
        %>
    </div>
</body>
</html>
