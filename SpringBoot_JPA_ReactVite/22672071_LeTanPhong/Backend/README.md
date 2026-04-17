# Backend - Library Management API

REST API Spring Boot để quản lý sách cho ứng dụng thư viện.

## Công nghệ
- Spring Boot
- Spring Web (REST API)
- Spring Data JPA
- Bean Validation
- MySQL
- CORS cho frontend React Vite
- Java 21

## Cấu trúc chính
- `entity/Book.java`: ánh xạ bảng `books`
- `dto/BookRequest.java`, `dto/BookResponse.java`: dữ liệu vào/ra
- `repository/BookRepository.java`: truy vấn JPA
- `service/BookService.java`, `service/impl/BookServiceImpl.java`: nghiệp vụ
- `controller/BookController.java`: REST endpoints
- `exception/*`: xử lý lỗi tập trung
- `config/CorsConfig.java`: cho phép frontend `localhost:5173`

## API
- `GET /api/books`
- `GET /api/books/{id}`
- `POST /api/books`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`
- `GET /api/books/search?keyword=...`

## Chạy chương trình
1. Tạo database MySQL `librarydb` hoặc chạy file `librarydb.sql`.
2. Cập nhật `src/main/resources/application.properties` nếu username/password MySQL khác mặc định.
3. Dự án dùng Java 21. Nếu `mvnw` đang trỏ sang JDK khác, hãy set `JAVA_HOME` về JDK 21 trước khi chạy.
4. Chạy backend:

```bash
mvn spring-boot:run
```

## Validate dữ liệu
- `title` không được rỗng
- `author` không được rỗng
- `quantity` không được âm
- `publishedYear` phải hợp lệ

## Ghi chú
- Backend trả dữ liệu JSON.
- Frontend React Vite có thể gọi trực tiếp API qua `fetch` hoặc `axios`.
- API lỗi trả về đơn giản theo dạng `{ "message": "..." }` để frontend hiển thị thông báo.

