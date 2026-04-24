package iuh.phong.backend.service.impl;

import iuh.phong.backend.dto.BookRequest;
import iuh.phong.backend.dto.BookResponse;
import iuh.phong.backend.entity.Book;
import iuh.phong.backend.repository.BookRepository;
import iuh.phong.backend.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Value("${app.upload-dir:upload}")
    private String uploadDir;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .sorted((left, right) -> Long.compare(left.getId(), right.getId()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sách với id = " + id));
        return toResponse(book);
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        validatePublishedYear(request.publishedYear());
        Book book = new Book();
        applyRequest(book, request);
        return toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        validatePublishedYear(request.publishedYear());
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sách với id = " + id));
        applyRequest(book, request);
        return toResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Không tìm thấy sách với id = " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String keyword) {
        return bookRepository.search(keyword == null ? "" : keyword.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void applyRequest(Book book, BookRequest request) {
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setCategory(blankToNull(request.category()));
        book.setPublisher(blankToNull(request.publisher()));
        book.setPublishedYear(request.publishedYear());
        book.setQuantity(request.quantity());
        book.setDescription(blankToNull(request.description()));
        book.setImageUrl(normalizeUploadUrl(request.imageUrl()));
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeUploadUrl(String value) {
        String normalized = blankToNull(value);
        if (normalized == null) {
            return null;
        }

        normalized = normalized.replace('\\', '/');
        if (normalized.contains("..")) {
            throw new IllegalArgumentException("imageUrl không hợp lệ");
        }

        if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
            return normalized;
        }

        if (normalized.startsWith("/upload/")) {
            return normalized;
        }

        if (normalized.startsWith("upload/")) {
            return "/" + normalized;
        }

        String fileName = normalized;
        int slashIndex = fileName.lastIndexOf('/');
        if (slashIndex >= 0) {
            fileName = fileName.substring(slashIndex + 1);
        }

        if (fileName.isBlank()) {
            throw new IllegalArgumentException("imageUrl không hợp lệ");
        }

        return "/upload/" + fileName;
    }

    private void validatePublishedYear(Integer publishedYear) {
        int currentYear = Year.now().getValue();
        if (publishedYear == null || publishedYear < 1000 || publishedYear > currentYear) {
            throw new IllegalArgumentException("publishedYear không hợp lệ");
        }
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublisher(),
                book.getPublishedYear(),
                book.getQuantity(),
                book.getDescription(),
                book.getImageUrl(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }

    @Override
    public String storeImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File ảnh không được rỗng");
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String safeName = Path.of(originalName).getFileName().toString().replace('\\', '/');
        if (safeName.contains("..")) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }

        int dot = safeName.lastIndexOf('.');
        String ext = dot >= 0 ? safeName.substring(dot) : "";
        String finalName = UUID.randomUUID() + ext;

        Path uploadPath = Path.of(uploadDir);
        Files.createDirectories(uploadPath);

        Path target = uploadPath.resolve(finalName);
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Không thể lưu file ảnh", ex);
        }

        return "/upload/" + finalName;
    }
}

