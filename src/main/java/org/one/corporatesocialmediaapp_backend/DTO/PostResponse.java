package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long author_id,
        Integer likeCount,
        Integer commentCount,
        Boolean isLikedByCurrentUser,
        List<CommentResponse> comments
) {
}

