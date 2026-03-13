package phong.lab5.controller;

import org.springframework.web.bind.annotation.*;
import phong.lab5.model.Student;
import phong.lab5.service.StudentService;

import java.util.List;

/**
 * Controller sử dụng Constructor Injection để inject StudentService.
 * Spring tự động wire StudentService bean (StudentServiceImpl) vào đây.
 */
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*") // Cho phép CORS từ mọi nguồn
public class StudentController {

    private final StudentService service;

    /**
     * Constructor Injection - Auto-wiring StudentService
     * Spring tự động tìm bean implement StudentService và inject vào
     */
    public StudentController(StudentService service) {
        this.service = service;
    }

    /**
     * POST /students - Thêm sinh viên mới
     * @param student đối tượng Student từ request body
     * @return Student đã được lưu
     */
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.save(student);
    }

    /**
     * GET /students - Lấy danh sách tất cả sinh viên
     * @return List<Student>
     */
    @GetMapping
    public List<Student> getAll() {
        return service.findAll();
    }

    /**
     * GET /students/{id} - Tìm sinh viên theo ID
     * @param id ID của sinh viên
     * @return Student hoặc null nếu không tìm thấy
     */
    @GetMapping("/{id}")
    public Student getById(@PathVariable Integer id) {
        return service.findById(id);
    }
}
