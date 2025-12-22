package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record LikeResponse(
        UserSummaryDTO user,
        LocalDateTime createdAt
) {
}

