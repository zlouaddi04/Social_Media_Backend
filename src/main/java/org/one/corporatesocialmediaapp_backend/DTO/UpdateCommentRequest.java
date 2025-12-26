package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @NotBlank(message = "Content is required and cannot be blank")
        String content,

        @NotNull(message = "Comment ID is required")
        Long commentId
) {
}

