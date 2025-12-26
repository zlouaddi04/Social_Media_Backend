package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserUpdateRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Full name is required and cannot be blank")
        String fullName,

        @NotBlank(message = "Profile picture is required and cannot be blank")
        String profilePicture,

        @NotNull(message = "Position is required")
        Position position,

        @NotNull(message = "Department is required")
        Department department
) {
}

