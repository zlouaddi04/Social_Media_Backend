package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String username,
        String fullName,
        String profilePicture,
        Position position,
        Department department,
        LocalDateTime createdAt,
        Long followerCount,
        Long followingCount,
        Boolean isFollowing
) {
}

