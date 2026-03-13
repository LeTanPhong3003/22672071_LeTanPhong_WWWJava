package com.example.student_crud.service;

import com.example.student_crud.model.Enrollment;
import com.example.student_crud.model.EnrollmentStatus;
import com.example.student_crud.model.Student;
import com.example.student_crud.model.Subject;
import com.example.student_crud.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment getById(Integer id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        enrollmentRepository.deleteById(id);
    }

    // Đăng ký môn học cho sinh viên
    public boolean enrollStudent(Integer studentId, Integer subjectId) {
        Student student = studentService.getById(studentId);
        Subject subject = subjectService.getById(subjectId);

        if (student == null || subject == null) {
            return false;
        }

        // Kiểm tra xem sinh viên đã đăng ký môn này chưa
        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndSubject(student, subject);
        if (existing.isPresent()) {
            return false; // Đã đăng ký rồi
        }

        Enrollment enrollment = new Enrollment(student, subject);
        enrollmentRepository.save(enrollment);
        return true;
    }

    // Hủy đăng ký môn học
    public boolean unenrollStudent(Integer studentId, Integer subjectId) {
        Student student = studentService.getById(studentId);
        Subject subject = subjectService.getById(subjectId);

        if (student == null || subject == null) {
            return false;
        }

        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndSubject(student, subject);
        if (enrollment.isPresent()) {
            enrollmentRepository.delete(enrollment.get());
            return true;
        }
        return false;
    }

    // Lấy danh sách môn học của sinh viên
    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentRepository.findEnrollmentsByStudentId(studentId);
    }

    // Lấy danh sách sinh viên đã đăng ký môn học
    public List<Enrollment> getEnrollmentsBySubject(Integer subjectId) {
        return enrollmentRepository.findEnrollmentsBySubjectId(subjectId);
    }

    // Lấy tất cả thông tin đăng ký với chi tiết
    public List<Enrollment> getAllEnrollmentsWithDetails() {
        return enrollmentRepository.findAllEnrollmentsWithDetails();
    }

    // Cập nhật điểm và trạng thái
    public boolean updateGradeAndStatus(Integer enrollmentId, Double grade, String status) {
        Enrollment enrollment = getById(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        enrollment.setGrade(grade);
        if (status != null && !status.isEmpty()) {
            try {
                // Sử dụng EnrollmentStatus.valueOf() trực tiếp
                enrollment.setStatus(EnrollmentStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        save(enrollment);
        return true;
    }
}
