# Backend

Spring Boot backend for the book management API.

## Security

The API now uses Spring Security with HTTP Basic auth and role-based access:

- `STUDENT`:
  - `GET /api/books`
  - `GET /api/books/{id}`
  - `GET /api/books/search`
- `BOOKKEEPER`:
  - all `STUDENT` permissions
  - `POST /api/books`
  - `PUT /api/books/{id}`
  - `DELETE /api/books/{id}`
  - `POST /api/books/upload`
- Public:
  - `GET /upload/**`

### Default accounts

- `student` / `student123` → `ROLE_STUDENT`
- `bookkeeper` / `bookkeeper123` → `ROLE_BOOKKEEPER`, `ROLE_STUDENT`

## Run tests on Windows

This project targets Java 21. If your `JAVA_HOME` points to another JDK, override it before running Maven Wrapper:

```powershell
Set-Location 'T:\22672071_LeTanPhong_WWWJava\SpringBoot_JPA_ReactVite\Backend'
$env:JAVA_HOME='C:\Program Files\Java\jdk-21'
.\mvnw.cmd test
```

## Run the app

```powershell
Set-Location 'T:\22672071_LeTanPhong_WWWJava\SpringBoot_JPA_ReactVite\Backend'
$env:JAVA_HOME='C:\Program Files\Java\jdk-21'
.\mvnw.cmd spring-boot:run
```

