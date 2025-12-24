package org.one.corporatesocialmediaapp_backend.DTO;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}

