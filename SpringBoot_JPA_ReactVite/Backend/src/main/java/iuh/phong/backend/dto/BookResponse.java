package iuh.phong.backend.dto;

import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        String category,
        String publisher,
        Integer publishedYear,
        Integer quantity,
        String description,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

