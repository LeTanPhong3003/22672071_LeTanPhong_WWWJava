package iuh.phong.backend.controller;

import iuh.phong.backend.config.SecurityConfig;
import iuh.phong.backend.dto.BookRequest;
import iuh.phong.backend.dto.BookResponse;
import iuh.phong.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = BookControllerSecurityTest.TestConfig.class)
@WebAppConfiguration
class BookControllerSecurityTest {

    private static final String VALID_BOOK_JSON = """
            {
              "title": "Clean Code",
              "author": "Robert C. Martin",
              "category": "Programming",
              "publisher": "Prentice Hall",
              "publishedYear": 2008,
              "quantity": 5,
              "description": "A handbook of agile software craftsmanship",
              "imageUrl": "/upload/clean-code.jpg"
            }
            """;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Configuration
    @EnableWebMvc
    @Import(SecurityConfig.class)
    static class TestConfig implements WebMvcConfigurer {

        @Bean
        BookService bookService() {
            return mock(BookService.class);
        }

        @Bean
        BookController bookController(BookService bookService) {
            return new BookController(bookService);
        }
    }

    @Autowired
    void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    void shouldRejectAnonymousReadAccess() throws Exception {
        when(bookService.getAllBooks()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowStudentReadAccess() throws Exception {
        when(bookService.getAllBooks()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/books")
                        .with(httpBasic("student", "student123")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAnonymousWriteAccess() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectNonAdminWriteAccess() throws Exception {
        mockMvc.perform(post("/api/books")
                        .with(httpBasic("student", "student123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowBookkeeperCreateBook() throws Exception {
        when(bookService.createBook(any(BookRequest.class))).thenReturn(new BookResponse(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "Programming",
                "Prentice Hall",
                2008,
                5,
                "A handbook of agile software craftsmanship",
                "/upload/clean-code.jpg",
                LocalDateTime.of(2026, 4, 24, 10, 0),
                LocalDateTime.of(2026, 4, 24, 10, 0)
        ));

        mockMvc.perform(post("/api/books")
                        .with(httpBasic("bookkeeper", "bookkeeper123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"));

        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    void shouldRejectAnonymousUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "sample-image".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/books/upload").file(file))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowBookkeeperUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "sample-image".getBytes(StandardCharsets.UTF_8)
        );
        when(bookService.storeImage(any(org.springframework.web.multipart.MultipartFile.class))).thenReturn("/upload/cover.jpg");

        mockMvc.perform(multipart("/api/books/upload")
                        .file(file)
                        .with(httpBasic("bookkeeper", "bookkeeper123")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("/upload/cover.jpg"));

        verify(bookService).storeImage(any(org.springframework.web.multipart.MultipartFile.class));
    }

    @Test
    void shouldRejectDeleteForNonAdmin() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .with(httpBasic("student", "student123")))
                .andExpect(status().isForbidden());
    }
}




