package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record PostSummaryDTO(
        Long id,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        Long author_id,
        Integer likeCount,
        Integer commentCount,
        Boolean isLikedByCurrentUser
) {
}

