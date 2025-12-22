package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserUpdateRequest(
        Long userId,
        String fullName,
        String profilePicture,
        Position position,
        Department department
) {
}

