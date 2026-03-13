package com.example.student_crud.controller;

import com.example.student_crud.model.Subject;
import com.example.student_crud.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("subjects", service.getAll());
        return "subjects";
    }

    @PostMapping("/save")
    public String save(Subject subject) {
        service.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/subjects";
    }

    // Endpoint tìm kiếm môn học
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) Integer credits,
                        Model model) {
        List<Subject> subjects;

        if (searchType != null && !searchType.isEmpty()) {
            switch (searchType) {
                case "code":
                    subjects = service.searchBySubjectCode(keyword);
                    break;
                case "name":
                    subjects = service.searchBySubjectName(keyword);
                    break;
                case "credits":
                    subjects = service.searchByCredits(credits);
                    break;
                default:
                    subjects = service.searchByKeyword(keyword);
            }
        } else {
            subjects = service.searchByKeyword(keyword);
        }

        model.addAttribute("subjects", subjects);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("credits", credits);
        model.addAttribute("searchPerformed", true);

        return "subjects";
    }
}
