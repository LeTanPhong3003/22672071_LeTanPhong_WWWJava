package phong.lab5.service;

import org.springframework.stereotype.Service;
import phong.lab5.model.Student;
import phong.lab5.repository.StudentRepository;

import java.util.List;

/**
 * StudentServiceImpl sử dụng Constructor Injection để inject StudentRepository.
 * Đây là cách thực hiện Dependency Injection được khuyến nghị nhất.
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    /**
     * Constructor Injection - Spring tự động inject StudentRepository bean vào đây.
     * BẮT BUỘC không được dùng: new StudentRepository()
     */
    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student save(Student student) {
        return repository.save(student);
    }

    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public Student findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
}

