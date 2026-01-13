package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserRegistrationRequest(
        @NotBlank(message = "Username is required and cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Email is required and cannot be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required and cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Full name is required and cannot be blank")
        String fullName,

        @NotNull(message = "Position is required")
        Position position,

        @NotNull(message = "Department is required")
        Department department
) {
}


