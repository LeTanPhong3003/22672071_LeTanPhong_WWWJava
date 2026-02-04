# CÂU HỎI BẮT BUỘC - QUẢN LÝ SINH VIÊN

## Câu 1: StudentRepository có phải là Bean không? Vì sao?

**Trả lời:** 
Có, StudentRepository là một Spring Bean.

**Lý do:**
- StudentRepository được đánh dấu bằng annotation `@Repository`
- Khi extend `JpaRepository`, Spring Data JPA tự động tạo một implementation proxy cho interface này
- Spring Container tự động quét và đăng ký StudentRepository như một Bean trong Application Context
- Do đó, chúng ta có thể inject nó vào các class khác thông qua DI (Dependency Injection)

---

## Câu 2: DI được sử dụng ở những class nào?

**Trả lời:**
DI (Dependency Injection) được sử dụng ở 2 class chính:

1. **StudentServiceImpl:**
   - Inject `StudentRepository` thông qua Constructor Injection
   ```java
   public StudentServiceImpl(StudentRepository repository) {
       this.repository = repository;
   }
   ```

2. **StudentController:**
   - Inject `StudentService` thông qua Constructor Injection
   ```java
   public StudentController(StudentService service) {
       this.service = service;
   }
   ```

**Lưu ý:** Cả 2 đều sử dụng Constructor Injection, không dùng `new` để khởi tạo đối tượng.

---

## Câu 3: Nếu có thêm MongoStudentRepository thì Spring có lỗi không? Vì sao?

**Trả lời:** 
Có, Spring sẽ báo lỗi.

**Lý do:**
- Nếu có thêm `MongoStudentRepository` cũng implement `StudentRepository` hoặc là một Repository khác cùng kiểu
- Spring sẽ có 2 Bean candidates để inject vào `StudentServiceImpl`
- Spring không biết chọn Bean nào → Lỗi: **"NoUniqueBeanDefinitionException"**

**Giải pháp:**
1. Sử dụng `@Primary` - đánh dấu Bean ưu tiên:
   ```java
   @Repository
   @Primary
   public class StudentRepository { }
   ```

2. Sử dụng `@Qualifier` - chỉ định Bean cụ thể:
   ```java
   public StudentServiceImpl(@Qualifier("studentRepository") StudentRepository repository) {
       this.repository = repository;
   }
   ```

---

## Câu 4: Constructor Injection có lợi ích gì so với Field Injection?

**Trả lời:**
Constructor Injection có nhiều lợi ích hơn Field Injection:

### Lợi ích của Constructor Injection:

1. **Immutability (Bất biến):**
   - Có thể khai báo field là `final`
   - Đảm bảo dependency không bị thay đổi sau khi khởi tạo
   ```java
   private final StudentRepository repository; // final
   ```

2. **Dễ dàng Testing:**
   - Có thể tạo đối tượng và inject mock/stub dependencies mà không cần Spring Container
   ```java
   StudentRepository mockRepo = mock(StudentRepository.class);
   StudentServiceImpl service = new StudentServiceImpl(mockRepo);
   ```

3. **Phát hiện lỗi sớm:**
   - Nếu thiếu dependency, lỗi sẽ xuất hiện ngay lúc compile/khởi tạo
   - Field Injection có thể chạy nhưng bị NullPointerException khi runtime

4. **Rõ ràng về Dependencies:**
   - Dễ thấy class phụ thuộc vào những Bean nào
   - Nếu có quá nhiều dependencies trong constructor → cảnh báo vi phạm Single Responsibility Principle

5. **Không cần Reflection:**
   - Constructor Injection hoạt động bình thường, không cần Spring reflection để inject
   - Field Injection cần reflection để set giá trị cho private field

### So sánh:

**Field Injection (không khuyến nghị):**
```java
@Autowired
private StudentRepository repository; // không thể final, khó test
```

**Constructor Injection (khuyến nghị):**
```java
private final StudentRepository repository;

public StudentServiceImpl(StudentRepository repository) {
    this.repository = repository;
}
```

---

## KẾT LUẬN

- DI giúp code dễ bảo trì, dễ test, và giảm coupling giữa các class
- Constructor Injection là best practice trong Spring Boot
- Không bao giờ dùng `new` để tạo Bean trong Spring application

