package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record PostSummaryDTO(
        Long id,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        UserSummaryDTO author,
        Long likeCount,
        Long commentCount,
        Boolean isLikedByCurrentUser
) {
}

