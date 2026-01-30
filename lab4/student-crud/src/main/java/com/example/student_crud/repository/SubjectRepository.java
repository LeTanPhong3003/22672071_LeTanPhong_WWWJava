package com.example.student_crud.repository;

import com.example.student_crud.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    // Tìm kiếm theo mã môn học
    List<Subject> findBySubjectCodeContainingIgnoreCase(String subjectCode);

    // Tìm kiếm theo tên môn học
    List<Subject> findBySubjectNameContainingIgnoreCase(String subjectName);

    // Tìm kiếm theo số tín chỉ
    List<Subject> findByCredits(Integer credits);

    // Tìm kiếm tổng hợp theo mã hoặc tên môn học
    @Query("SELECT s FROM Subject s WHERE " +
           "LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Subject> findBySubjectCodeOrNameContaining(@Param("keyword") String keyword);
}
