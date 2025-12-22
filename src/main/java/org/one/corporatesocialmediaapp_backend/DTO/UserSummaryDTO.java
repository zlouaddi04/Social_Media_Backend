package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserSummaryDTO(
        Long id,
        String username,
        String fullName,
        String profilePicture,
        Position position,
        Department department,
        Boolean isFollowing,
        Long mutualConnectionsCount
) {
}

