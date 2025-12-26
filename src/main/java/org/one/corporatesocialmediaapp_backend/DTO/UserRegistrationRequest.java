package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserRegistrationRequest(
        @NotBlank(message = "Username is required and cannot be blank")
        String username,

        @NotBlank(message = "Email is required and cannot be blank")
        String email,

        @NotBlank(message = "Password is required and cannot be blank")
        String password,

        @NotBlank(message = "Full name is required and cannot be blank")
        String fullName,

        @NotNull(message = "Position is required")
        Position position,

        @NotNull(message = "Department is required")
        Department department
) {
}


