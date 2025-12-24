package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotNull
        Long userId,

        @NotBlank
        String content,

        @NotBlank
        String imageUrl
) {
}

