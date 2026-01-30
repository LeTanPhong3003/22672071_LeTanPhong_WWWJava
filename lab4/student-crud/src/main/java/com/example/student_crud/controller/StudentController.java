package com.example.student_crud.controller;

import com.example.student_crud.model.Student;
import com.example.student_crud.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", service.getAll());
        return "students";
    }

    @PostMapping("/save")
    public String save(Student student) {
        service.save(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/students";
    }

    // Endpoint tìm kiếm tổng quát
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) Integer age,
                        Model model) {
        List<Student> students;

        if (searchType != null && !searchType.isEmpty()) {
            switch (searchType) {
                case "name":
                    students = service.searchByName(keyword);
                    break;
                case "email":
                    students = service.searchByEmail(keyword);
                    break;
                case "age":
                    students = service.searchByAge(age);
                    break;
                default:
                    students = service.searchByKeyword(keyword);
            }
        } else {
            students = service.searchByKeyword(keyword);
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("age", age);
        model.addAttribute("searchPerformed", true);

        return "students";
    }
}

@Controller
class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }
}
