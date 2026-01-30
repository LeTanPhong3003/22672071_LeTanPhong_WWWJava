package com.example.student_crud.service;

import com.example.student_crud.model.Subject;
import com.example.student_crud.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    public List<Subject> getAll() {
        return repository.findAll();
    }

    public Subject save(Subject subject) {
        return repository.save(subject);
    }

    public Subject getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // Các phương thức tìm kiếm
    public List<Subject> searchBySubjectCode(String subjectCode) {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            return getAll();
        }
        return repository.findBySubjectCodeContainingIgnoreCase(subjectCode);
    }

    public List<Subject> searchBySubjectName(String subjectName) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            return getAll();
        }
        return repository.findBySubjectNameContainingIgnoreCase(subjectName);
    }

    public List<Subject> searchByCredits(Integer credits) {
        if (credits == null) {
            return getAll();
        }
        return repository.findByCredits(credits);
    }

    public List<Subject> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return repository.findBySubjectCodeOrNameContaining(keyword);
    }
}
