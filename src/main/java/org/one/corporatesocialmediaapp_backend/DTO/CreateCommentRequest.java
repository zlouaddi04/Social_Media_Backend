package org.one.corporatesocialmediaapp_backend.DTO;

public record CreateCommentRequest(
        String content,
        Long postId
) {
}

