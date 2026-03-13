CREATE DATABASE IF NOT EXISTS librarydb;

USE librarydb;



-- Xóa bảng nếu đã tồn tại

DROP TABLE IF EXISTS books;

DROP TABLE IF EXISTS categories;



-- Tạo bảng categories

CREATE TABLE categories (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL

);



-- Tạo bảng books

CREATE TABLE books (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    title VARCHAR(200) NOT NULL,

    author VARCHAR(150) NOT NULL,

    price DOUBLE NOT NULL,

    published_year INT,

    category_id BIGINT,

    CONSTRAINT fk_book_category

        FOREIGN KEY (category_id)

        REFERENCES categories(id)

        ON DELETE SET NULL

        ON UPDATE CASCADE

);



-- Dữ liệu mẫu cho categories

INSERT INTO categories(name) VALUES

('Programming'),

('Database'),

('Web Development'),

('Networking');



-- Dữ liệu mẫu cho books

INSERT INTO books(title, author, price, published_year, category_id) VALUES

('Spring Boot Basics', 'John Smith', 120000, 2023, 1),

('Learning MySQL', 'David Lee', 95000, 2022, 2),

('Thymeleaf in Action', 'Anna Brown', 110000, 2024, 3),

('Computer Networks', 'James Wilson', 130000, 2021, 4);

