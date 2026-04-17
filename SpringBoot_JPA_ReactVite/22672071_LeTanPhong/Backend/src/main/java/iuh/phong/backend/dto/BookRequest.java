package iuh.phong.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "title không được rỗng")
        @Size(max = 255, message = "title tối đa 255 ký tự")
        String title,

        @NotBlank(message = "author không được rỗng")
        @Size(max = 150, message = "author tối đa 150 ký tự")
        String author,

        @Size(max = 100, message = "category tối đa 100 ký tự")
        String category,

        @Size(max = 150, message = "publisher tối đa 150 ký tự")
        String publisher,

        @NotNull(message = "publishedYear không được rỗng")
        @Min(value = 1000, message = "publishedYear không hợp lệ")
        Integer publishedYear,

        @NotNull(message = "quantity không được rỗng")
        @Min(value = 0, message = "quantity không được âm")
        Integer quantity,

        String description,

        @Size(max = 255, message = "imageUrl tối đa 255 ký tự")
        String imageUrl
) {
}

