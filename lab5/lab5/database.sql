-- ========================================
-- SCRIPT TẠO DATABASE VÀ BẢNG STUDENTS
-- ========================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS studentdb;

-- Sử dụng database
USE studentdb;

-- Tạo bảng students
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    age INT
);

-- Insert dữ liệu mẫu (optional)
INSERT INTO students (name, email, age) VALUES
('Nguyen Van A', 'vana@example.com', 20),
('Tran Thi B', 'thib@example.com', 21),
('Le Van C', 'vanc@example.com', 22);

-- Kiểm tra dữ liệu
SELECT * FROM students;

