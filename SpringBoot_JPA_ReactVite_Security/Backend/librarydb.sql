CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

CREATE TABLE IF NOT EXISTS books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    category VARCHAR(100),
    publisher VARCHAR(150),
    published_year INT,
    quantity INT DEFAULT 0,
    description TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);




-- Sample data imported from provided image (including created_at and updated_at)
INSERT INTO books (id, title, author, category, publisher, published_year, quantity, description, image_url, created_at, updated_at)
VALUES
    (1, 'Clean Code', 'Robert C. Martin', 'Programming', 'Prentice Hall', 2008, 10, 'A Handbook of Agile Software Craftsmanship', '/upload/5e050adb-8d04-4697-b9be-5b3c212371fe.jpg', '2026-04-17 16:20:11', '2026-04-17 10:10:23'),
    (2, 'The Pragmatic Programmer', 'Andrew Hunt', 'Programming', 'Addison-Wesley', 1999, 8, 'Your Journey to Mastery', '/upload/20dfb218-ba33-4dd8-b945-17809348a55a.jpg', '2026-04-17 16:20:11', '2026-04-17 10:12:32'),
    (3, 'Atomic Habits', 'James Clear', 'Self-help', 'Avery', 2018, 15, 'Build good habits and break bad ones', '/upload/63c24826-5de5-41e9-9b1d-3ed594280a63.jpg', '2026-04-17 16:20:11', '2026-04-17 10:12:58'),
    (4, 'Rich Dad Poor Dad', 'Robert T. Kiyosaki', 'Finance', 'Plata Publishing', 1997, 12, 'Financial education book', '/upload/74c06e1f-6de9-4b2a-a9da-bd19ae3ef12e.webp', '2026-04-17 16:20:11', '2026-04-17 10:12:50'),
    (5, 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 'J.B. Lippincott & Co.', 1960, 5, 'Classic novel of racism and injustice', '/upload/0b49e40e-9f0a-4087-a6a8-875487bbf75d.jpg', '2026-04-17 16:20:11', '2026-04-17 10:12:40');



