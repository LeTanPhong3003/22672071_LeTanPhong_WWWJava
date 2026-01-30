package com.example.student_crud.repository;

import com.example.student_crud.model.Enrollment;
import com.example.student_crud.model.Student;
import com.example.student_crud.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // Tìm tất cả đăng ký của một sinh viên
    List<Enrollment> findByStudent(Student student);

    // Tìm tất cả đăng ký của một môn học
    List<Enrollment> findBySubject(Subject subject);

    // Tìm đăng ký cụ thể của sinh viên và môn học
    Optional<Enrollment> findByStudentAndSubject(Student student, Subject subject);

    // Tìm đăng ký theo trạng thái
    List<Enrollment> findByStatus(String status);

    // Lấy danh sách môn học đã đăng ký của sinh viên
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.subject WHERE e.student.id = :studentId")
    List<Enrollment> findEnrollmentsByStudentId(@Param("studentId") Integer studentId);

    // Lấy danh sách sinh viên đã đăng ký môn học
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.subject.id = :subjectId")
    List<Enrollment> findEnrollmentsBySubjectId(@Param("subjectId") Integer subjectId);

    // View thông tin đăng ký chi tiết
    @Query("SELECT e FROM Enrollment e " +
           "JOIN FETCH e.student s " +
           "JOIN FETCH e.subject sub " +
           "ORDER BY s.name, sub.subjectCode")
    List<Enrollment> findAllEnrollmentsWithDetails();
}
