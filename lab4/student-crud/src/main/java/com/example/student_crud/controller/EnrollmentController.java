package com.example.student_crud.controller;

import com.example.student_crud.service.EnrollmentService;
import com.example.student_crud.service.StudentService;
import com.example.student_crud.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllEnrollmentsWithDetails());
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("subjects", subjectService.getAll());
        return "enrollments";
    }

    // Đăng ký môn học cho sinh viên
    @PostMapping("/enroll")
    public String enroll(@RequestParam Integer studentId, @RequestParam Integer subjectId, Model model) {
        boolean success = enrollmentService.enrollStudent(studentId, subjectId);
        if (!success) {
            model.addAttribute("error", "Không thể đăng ký môn học. Sinh viên có thể đã đăng ký môn này rồi.");
        }
        return "redirect:/enrollments";
    }

    // Hủy đăng ký môn học
    @GetMapping("/unenroll")
    public String unenroll(@RequestParam Integer studentId, @RequestParam Integer subjectId) {
        enrollmentService.unenrollStudent(studentId, subjectId);
        return "redirect:/enrollments";
    }

    // Xóa đăng ký
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        enrollmentService.delete(id);
        return "redirect:/enrollments";
    }

    // Xem môn học của một sinh viên
    @GetMapping("/student/{studentId}")
    public String viewStudentEnrollments(@PathVariable Integer studentId, Model model) {
        model.addAttribute("student", studentService.getById(studentId));
        model.addAttribute("enrollments", enrollmentService.getEnrollmentsByStudent(studentId));
        return "student-enrollments";
    }

    // Xem sinh viên đã đăng ký một môn học
    @GetMapping("/subject/{subjectId}")
    public String viewSubjectEnrollments(@PathVariable Integer subjectId, Model model) {
        model.addAttribute("subject", subjectService.getById(subjectId));
        model.addAttribute("enrollments", enrollmentService.getEnrollmentsBySubject(subjectId));
        return "subject-enrollments";
    }

    // Cập nhật điểm và trạng thái
    @PostMapping("/update-grade")
    public String updateGrade(@RequestParam Integer enrollmentId,
                             @RequestParam Double grade,
                             @RequestParam String status) {
        enrollmentService.updateGradeAndStatus(enrollmentId, grade, status);
        return "redirect:/enrollments";
    }
}
