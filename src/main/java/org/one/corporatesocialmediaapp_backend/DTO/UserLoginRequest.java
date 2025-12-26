package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "Email is required and cannot be blank")
        String email,

        @NotBlank(message = "Password is required and cannot be blank")
        String password
) {
}

