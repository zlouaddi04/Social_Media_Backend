package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record FollowerListResponse(
        UserSummaryDTO user,
        LocalDateTime followedAt,
        Boolean isFollowingBack
) {
}

