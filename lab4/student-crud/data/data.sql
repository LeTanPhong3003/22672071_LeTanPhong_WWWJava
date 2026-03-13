-- File data.sql chứa dữ liệu mẫu cho bảng students
-- Clear bảng trước khi tạo
DROP DATABASE IF EXISTS springboot_crud;
-- Tạo database nếu chưa có
CREATE DATABASE IF NOT EXISTS springboot_crud;
USE springboot_crud;

-- Tạo bảng students nếu chưa có
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INT
);

-- Tạo bảng subjects (môn học)
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    subject_name VARCHAR(255) NOT NULL,
    credits INT NOT NULL DEFAULT 3,
    description TEXT
);

-- Tạo bảng enrollments (đăng ký môn học) - Many-to-Many relationship
CREATE TABLE IF NOT EXISTS enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    enrollment_date DATE DEFAULT (CURDATE()),
    status ENUM('REGISTERED', 'COMPLETED', 'DROPPED') DEFAULT 'REGISTERED',
    grade DECIMAL(3,1) DEFAULT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, subject_id)
);

-- Insert dữ liệu mẫu cho students
INSERT INTO students (name, email, age) VALUES
('Nguyễn Văn An', 'an.nguyen@email.com', 20),
('Trần Thị Bình', 'binh.tran@email.com', 21),
('Lê Văn Cường', 'cuong.le@email.com', 19),
('Phạm Thị Dung', 'dung.pham@email.com', 22),
('Hoàng Văn Em', 'em.hoang@email.com', 20),
('Võ Thị Phương', 'phuong.vo@email.com', 23),
('Đặng Văn Giang', 'giang.dang@email.com', 21),
('Bùi Thị Hoa', 'hoa.bui@email.com', 19),
('Lý Văn Inh', 'inh.ly@email.com', 24),
('Trịnh Thị Kiều', 'kieu.trinh@email.com', 22),
('Ngô Văn Lâm', 'lam.ngo@email.com', 20),
('Phan Thị Mai', 'mai.phan@email.com', 21),
('Vũ Văn Nam', 'nam.vu@email.com', 25),
('Đỗ Thị Oanh', 'oanh.do@email.com', 19),
('Tôn Văn Phúc', 'phuc.ton@email.com', 23),
('Đinh Thị Quỳnh', 'quynh.dinh@email.com', 20),
('Hồ Văn Rộng', 'rong.ho@email.com', 22),
('Mai Thị Sương', 'suong.mai@email.com', 21),
('Cao Văn Tài', 'tai.cao@email.com', 24),
('Lưu Thị Uyên', 'uyen.luu@email.com', 19);

-- Insert dữ liệu mẫu cho subjects (môn học)
INSERT INTO subjects (subject_code, subject_name, credits, description) VALUES
('IT001', 'Lập trình Java', 3, 'Môn học về lập trình hướng đối tượng với Java'),
('IT002', 'Cơ sở dữ liệu', 3, 'Thiết kế và quản lý cơ sở dữ liệu'),
('IT003', 'Phát triển Web', 3, 'Phát triển ứng dụng web với HTML, CSS, JavaScript'),
('IT004', 'Mạng máy tính', 3, 'Kiến thức cơ bản về mạng và giao thức'),
('IT005', 'Hệ điều hành', 3, 'Nguyên lý hoạt động của hệ điều hành'),
('IT006', 'Kỹ thuật phần mềm', 3, 'Quy trình phát triển phần mềm'),
('IT007', 'Trí tuệ nhân tạo', 4, 'Giới thiệu về AI và Machine Learning'),
('IT008', 'Bảo mật thông tin', 3, 'Các kỹ thuật bảo mật dữ liệu'),
('IT009', 'Phân tích thiết kế hệ thống', 3, 'Phân tích và thiết kế hệ thống thông tin'),
('IT010', 'Lập trình di động', 4, 'Phát triển ứng dụng cho thiết bị di động');

-- Insert dữ liệu mẫu cho enrollments (đăng ký môn học)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, status, grade) VALUES
-- Nguyễn Văn An (ID: 1)
(1, 1, '2024-01-15', 'COMPLETED', 8.5),
(1, 2, '2024-01-15', 'COMPLETED', 7.8),
(1, 3, '2024-02-01', 'REGISTERED', NULL),

-- Trần Thị Bình (ID: 2)
(2, 1, '2024-01-15', 'COMPLETED', 9.0),
(2, 3, '2024-01-15', 'REGISTERED', NULL),
(2, 4, '2024-02-01', 'REGISTERED', NULL),

-- Lê Văn Cường (ID: 3)
(3, 2, '2024-01-20', 'COMPLETED', 8.2),
(3, 5, '2024-01-20', 'REGISTERED', NULL),
(3, 6, '2024-02-01', 'REGISTERED', NULL),

-- Phạm Thị Dung (ID: 4)
(4, 1, '2024-01-18', 'COMPLETED', 8.8),
(4, 7, '2024-01-18', 'REGISTERED', NULL),
(4, 8, '2024-02-01', 'REGISTERED', NULL),

-- Hoàng Văn Em (ID: 5)
(5, 2, '2024-01-22', 'COMPLETED', 7.5),
(5, 3, '2024-01-22', 'REGISTERED', NULL),
(5, 9, '2024-02-01', 'REGISTERED', NULL),

-- Võ Thị Phương (ID: 6)
(6, 4, '2024-01-25', 'COMPLETED', 9.2),
(6, 5, '2024-01-25', 'REGISTERED', NULL),
(6, 10, '2024-02-01', 'REGISTERED', NULL),

-- Thêm một số đăng ký khác
(7, 1, '2024-01-28', 'REGISTERED', NULL),
(7, 6, '2024-01-28', 'REGISTERED', NULL),
(8, 2, '2024-01-30', 'REGISTERED', NULL),
(8, 7, '2024-01-30', 'REGISTERED', NULL),
(9, 3, '2024-02-01', 'REGISTERED', NULL),
(9, 8, '2024-02-01', 'REGISTERED', NULL),
(10, 4, '2024-02-02', 'REGISTERED', NULL),
(10, 9, '2024-02-02', 'REGISTERED', NULL);

-- Hiển thị dữ liệu đã insert
SELECT * FROM students ORDER BY id;
SELECT * FROM subjects ORDER BY id;
SELECT * FROM enrollments ORDER BY id;

-- View để xem thông tin đăng ký chi tiết
SELECT
    s.name as student_name,
    sub.subject_code,
    sub.subject_name,
    e.enrollment_date,
    e.status,
    e.grade
FROM enrollments e
JOIN students s ON e.student_id = s.id
JOIN subjects sub ON e.subject_id = sub.id
ORDER BY s.name, sub.subject_code;
