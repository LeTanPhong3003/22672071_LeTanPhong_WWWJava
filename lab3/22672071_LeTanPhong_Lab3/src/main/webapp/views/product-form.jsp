<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("product") == null ? "Thêm sản phẩm" : "Sửa sản phẩm" %></title>
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
            max-width: 600px;
            margin: 50px auto;
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 30px;
            text-align: center;
        }
        form {
            margin-top: 20px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #34495e;
            font-weight: bold;
        }
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #bdc3c7;
            border-radius: 4px;
            font-size: 14px;
        }
        input[type="text"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #3498db;
        }
        .required {
            color: #e74c3c;
        }
        .form-actions {
            margin-top: 30px;
            text-align: center;
        }
        .btn {
            display: inline-block;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            border: none;
            cursor: pointer;
            margin: 5px;
            transition: all 0.3s;
        }
        .btn-submit {
            background-color: #3498db;
            color: white;
        }
        .btn-submit:hover {
            background-color: #2980b9;
        }
        .btn-cancel {
            background-color: #95a5a6;
            color: white;
        }
        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
        .hint {
            font-size: 12px;
            color: #7f8c8d;
            margin-top: 5px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <%
            Product product = (Product) request.getAttribute("product");
            boolean isEdit = (product != null);
            String error = request.getParameter("error");
        %>

        <h1><%= isEdit ? "Sửa sản phẩm" : "Thêm sản phẩm mới" %></h1>

        <% if ("failed".equals(error)) { %>
        <div class="error">
            ✗ Thao tác thất bại! Vui lòng thử lại.
        </div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/<%= isEdit ? "update" : "insert" %>">
            <% if (isEdit) { %>
            <input type="hidden" name="id" value="<%= product.getId() %>">
            <% } %>

            <div class="form-group">
                <label for="name">Tên sản phẩm <span class="required">*</span></label>
                <input type="text"
                       id="name"
                       name="name"
                       value="<%= isEdit ? product.getName() : "" %>"
                       required
                       placeholder="Nhập tên sản phẩm">
                <div class="hint">Ví dụ: Laptop Dell XPS 15, iPhone 15 Pro</div>
            </div>

            <div class="form-group">
                <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                <input type="number"
                       id="price"
                       name="price"
                       value="<%= isEdit ? String.format("%.0f", product.getPrice()) : "" %>"
                       required
                       min="0"
                       step="1000"
                       placeholder="Nhập giá sản phẩm">
                <div class="hint">Nhập giá bằng số, ví dụ: 25000000</div>
            </div>

            <div class="form-group">
                <label for="url_image">URL Hình ảnh</label>
                <input type="text"
                       id="url_image"
                       name="urlImage"
                       value="<%= isEdit && product.getUrlImage() != null ? product.getUrlImage() : "" %>"
                       placeholder="https://example.com/image.jpg">
                <div class="hint">Nhập đường dẫn URL của hình ảnh sản phẩm (không bắt buộc)</div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-submit">
                    <%= isEdit ? "Cập nhật" : "Thêm mới" %>
                </button>
                <a href="${pageContext.request.contextPath}/list" class="btn btn-cancel">Hủy</a>
            </div>
        </form>
    </div>

    <script>
        // Validate form
        document.querySelector('form').addEventListener('submit', function(e) {
            var name = document.getElementById('name').value.trim();
            var price = document.getElementById('price').value;

            if (name === '') {
                alert('Vui lòng nhập tên sản phẩm!');
                e.preventDefault();
                return false;
            }

            if (price <= 0) {
                alert('Giá sản phẩm phải lớn hơn 0!');
                e.preventDefault();
                return false;
            }

            return true;
        });
    </script>
</body>
</html>
