package phong.lab6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phong.lab6.entity.Book;
import phong.lab6.entity.Category;
import phong.lab6.repository.IBookRepository;
import phong.lab6.repository.ICategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/list";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/edit";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book) {
        book.setId(id);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword, Model model) {
        List<Book> books = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/list";
    }

    @GetMapping("/filter")
    public String filterBooksByCategory(@RequestParam(name = "categoryId", required = false) Long categoryId, Model model) {
        if (categoryId == null) {
            model.addAttribute("warning", "Please select a category to filter.");
            model.addAttribute("books", bookRepository.findAll());
        } else {
            List<Book> books = bookRepository.findByCategory_Id(categoryId);
            model.addAttribute("books", books);
        }
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/list";
    }
}
