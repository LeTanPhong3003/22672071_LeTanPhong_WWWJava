package com.example.student_crud.repository;

import com.example.student_crud.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository
        extends JpaRepository<Student, Integer> {

    // Tìm kiếm theo tên (không phân biệt hoa thường)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Tìm kiếm theo email (không phân biệt hoa thường)
    List<Student> findByEmailContainingIgnoreCase(String email);

    // Tìm kiếm theo tuổi
    List<Student> findByAge(Integer age);

    // Tìm kiếm tổng hợp theo tên hoặc email
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> findByNameOrEmailContaining(@Param("keyword") String keyword);
}
