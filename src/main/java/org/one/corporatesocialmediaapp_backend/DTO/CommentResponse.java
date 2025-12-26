package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        UserSummaryDTO userSummary,
        Boolean isCommentOwner
) {
}

