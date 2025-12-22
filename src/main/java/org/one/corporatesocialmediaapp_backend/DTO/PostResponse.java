package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserSummaryDTO author,
        Long likeCount,
        Long commentCount,
        Boolean isLikedByCurrentUser,
        List<CommentResponse> comments
) {
}

