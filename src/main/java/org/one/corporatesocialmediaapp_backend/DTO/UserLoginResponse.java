package org.one.corporatesocialmediaapp_backend.DTO;

public record UserLoginResponse(
        String token,
        UserSummaryDTO userSummary
) {
}
