package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record ConnectionResponse(
        Long id,
        UserSummaryDTO follower,
        UserSummaryDTO following,
        LocalDateTime createdAt
) {
}

