package phong.lab5.service;

import phong.lab5.model.Student;
import java.util.List;

/**
 * Interface định nghĩa các business logic cho Student
 */
public interface StudentService {

    Student save(Student student);

    List<Student> findAll();

    Student findById(Integer id);
}

