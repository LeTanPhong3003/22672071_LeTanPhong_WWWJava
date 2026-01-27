-- Script SQL để tạo database và bảng products

CREATE DATABASE IF NOT EXISTS shop_db;
USE shop_db;

-- Tạo bảng products
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    url_image VARCHAR(500)
);

-- Chèn dữ liệu mẫu
INSERT INTO products (name, price, url_image) VALUES
('Laptop Dell XPS', 25000000, 'https://link-anh-1.jpg'),
('iPhone 15 Pro', 28000000, 'https://link-anh-2.jpg');

-- Hiển thị dữ liệu
SELECT * FROM products;
