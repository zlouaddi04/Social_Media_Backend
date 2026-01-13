package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotNull(message = "User ID is required")
        Long user_db_Id,

        @NotBlank(message = "Content is required and cannot be blank")
        String content,

        String imageUrl // Optional
) {
}

