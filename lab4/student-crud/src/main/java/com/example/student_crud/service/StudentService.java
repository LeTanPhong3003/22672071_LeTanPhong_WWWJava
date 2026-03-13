package com.example.student_crud.service;

import com.example.student_crud.model.Student;
import com.example.student_crud.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public Student getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // Các phương thức tìm kiếm mới
    public List<Student> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAll();
        }
        return repository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> searchByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return getAll();
        }
        return repository.findByEmailContainingIgnoreCase(email);
    }

    public List<Student> searchByAge(Integer age) {
        if (age == null) {
            return getAll();
        }
        return repository.findByAge(age);
    }

    public List<Student> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return repository.findByNameOrEmailContaining(keyword);
    }
}
