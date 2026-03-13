package phong.lab6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import phong.lab6.entity.Category;
import phong.lab6.repository.ICategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryRepository categoryRepository;

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String categoryName) {
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        categoryRepository.save(newCategory);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        if (category.getBooks() == null || category.getBooks().isEmpty()) {
            categoryRepository.deleteById(id);
        }
        return "redirect:/categories";
    }
}
