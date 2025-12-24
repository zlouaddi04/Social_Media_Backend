package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserUpdateRequest(
        @NotNull
        Long userId,

        @NotBlank
        String fullName,

        @NotBlank
        String profilePicture,

        @NotNull
        Position position,

        @NotNull
        Department department
) {
}

