package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserRegistrationRequest(
        String username,
        String email,
        String password,
        String fullName,
        Position position,
        Department department
) {
}


