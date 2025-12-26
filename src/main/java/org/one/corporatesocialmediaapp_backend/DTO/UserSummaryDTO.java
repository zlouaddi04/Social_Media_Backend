package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;

public record UserSummaryDTO(
        Long id,
        String username,
        String fullname
) {
}

