package org.one.corporatesocialmediaapp_backend.DTO;

import java.time.LocalDateTime;

public record FollowingListResponse(
        UserSummaryDTO user,
        LocalDateTime followingSince,
        Boolean isFollower
) {
}

