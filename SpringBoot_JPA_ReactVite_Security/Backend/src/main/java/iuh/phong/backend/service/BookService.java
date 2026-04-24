package iuh.phong.backend.service;

import iuh.phong.backend.dto.BookRequest;
import iuh.phong.backend.dto.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    List<BookResponse> getAllBooks();

    BookResponse getBookById(Long id);

    BookResponse createBook(BookRequest request);

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    List<BookResponse> searchBooks(String keyword);

    String storeImage(MultipartFile file) throws Exception;
}

