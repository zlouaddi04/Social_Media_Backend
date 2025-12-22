package org.one.corporatesocialmediaapp_backend.DTO;

public record CreatePostRequest(
        String content,
        String imageUrl
) {
}

