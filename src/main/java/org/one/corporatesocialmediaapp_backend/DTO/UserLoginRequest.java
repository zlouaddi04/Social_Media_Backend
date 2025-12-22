package org.one.corporatesocialmediaapp_backend.DTO;

public record UserLoginRequest(
        String emailOrUsername,
        String password
) {
}

