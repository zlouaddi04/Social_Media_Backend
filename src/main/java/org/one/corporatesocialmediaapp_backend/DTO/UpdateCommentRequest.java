package org.one.corporatesocialmediaapp_backend.DTO;

public record UpdateCommentRequest(
        String content,
        Long postId,
        Long commentId
) {
}

