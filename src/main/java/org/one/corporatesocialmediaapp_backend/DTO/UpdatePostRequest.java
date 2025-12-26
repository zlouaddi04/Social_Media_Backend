package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePostRequest(
        @NotNull(message = "Post ID is required")
        @Min(value = 1, message = "Post ID must be at least 1")
        Long db_id,

        @NotBlank(message = "Content is required and cannot be blank")
        String content,

        @NotBlank(message = "Image URL is required and cannot be blank")
        String imageUrl
) {
}

