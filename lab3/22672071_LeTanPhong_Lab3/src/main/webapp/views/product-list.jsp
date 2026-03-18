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
        .filter-bar {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border: 1px solid #ecf0f1;
            border-radius: 8px;
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: end;
        }
        .filter-group {
            flex: 1;
            min-width: 180px;
        }
        .filter-group label {
            display: block;
            font-size: 13px;
            color: #34495e;
            margin-bottom: 6px;
            font-weight: bold;
        }
        .filter-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #bdc3c7;
            border-radius: 4px;
            font-size: 14px;
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
        .btn-filter {
            background-color: #3498db;
            color: white;
            border: none;
            cursor: pointer;
        }
        .btn-filter:hover {
            background-color: #2980b9;
        }
        .btn-reset {
            background-color: #95a5a6;
            color: white;
        }
        .btn-reset:hover {
            background-color: #7f8c8d;
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
        .thumbnail {
            width: 90px;
            height: 90px;
            object-fit: cover;
            border-radius: 6px;
            border: 1px solid #dfe6e9;
            display: block;
        }
        .thumbnail-fallback {
            color: #95a5a6;
            font-style: italic;
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

        <%
            String keyword = (String) request.getAttribute("keyword");
            Double minPrice = (Double) request.getAttribute("minPrice");
            Double maxPrice = (Double) request.getAttribute("maxPrice");
        %>

        <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/list">
            <div class="filter-group">
                <label for="keyword">Tìm theo tên</label>
                <input type="text" id="keyword" name="keyword"
                       value="<%= keyword != null ? keyword : "" %>"
                       placeholder="Nhập tên sản phẩm...">
            </div>
            <div class="filter-group">
                <label for="minPrice">Giá từ (VNĐ)</label>
                <input type="number" id="minPrice" name="minPrice" min="0" step="1000"
                       value="<%= minPrice != null ? String.format("%.0f", minPrice) : "" %>">
            </div>
            <div class="filter-group">
                <label for="maxPrice">Đến giá (VNĐ)</label>
                <input type="number" id="maxPrice" name="maxPrice" min="0" step="1000"
                       value="<%= maxPrice != null ? String.format("%.0f", maxPrice) : "" %>">
            </div>
            <div>
                <button type="submit" class="btn btn-filter">Lọc</button>
                <a href="${pageContext.request.contextPath}/list" class="btn btn-reset">Đặt lại</a>
            </div>
        </form>

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
                    <th>Hình ảnh</th>
                    <th style="text-align: center;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Product product : products) {
                        String imageValue = product.getUrlImage();
                        String imageSrc = imageValue;
                        if (imageValue != null) {
                            String trimmed = imageValue.trim();
                            if (trimmed.startsWith("uploads/")) {
                                imageSrc = request.getContextPath() + "/" + trimmed;
                            } else if (trimmed.startsWith("/uploads/")) {
                                imageSrc = request.getContextPath() + trimmed;
                            } else {
                                imageSrc = trimmed;
                            }
                        }
                %>
                <tr>
                    <td><%= product.getId() %></td>
                    <td><%= product.getName() %></td>
                    <td class="price"><%= String.format("%,.0f", product.getPrice()) %></td>
                    <td>
                        <% if (imageSrc != null && !imageSrc.isEmpty()) { %>
                            <img
                                src="<%= imageSrc %>"
                                alt="<%= product.getName() %>"
                                class="thumbnail js-thumbnail">
                            <span class="thumbnail-fallback" style="display:none;">URL ảnh không hợp lệ</span>
                        <% } else { %>
                            <span class="thumbnail-fallback">Không có ảnh</span>
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

    <script>
        (function() {
            var thumbnails = document.querySelectorAll('.js-thumbnail');
            thumbnails.forEach(function(img) {
                img.addEventListener('error', function() {
                    img.style.display = 'none';
                    if (img.nextElementSibling) {
                        img.nextElementSibling.style.display = 'inline';
                    }
                });
            });
        })();
    </script>
</body>
</html>
