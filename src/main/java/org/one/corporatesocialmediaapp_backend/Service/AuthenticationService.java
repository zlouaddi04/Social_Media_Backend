package org.one.corporatesocialmediaapp_backend.Service;

import org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions.AuthUnauthorizedException;
import org.one.corporatesocialmediaapp_backend.Models.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    /**
     * Get the currently authenticated user from the security context
     * @return CustomUserDetails of the authenticated user
     * @throws AuthUnauthorizedException if no authenticated user is found
     */
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AuthUnauthorizedException("No authenticated user found");
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    /**
     * Get the ID of the currently authenticated user
     * @return User ID
     * @throws AuthUnauthorizedException if no authenticated user is found
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Get the username of the currently authenticated user
     * @return Username
     * @throws AuthUnauthorizedException if no authenticated user is found
     */
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * Check if the current user matches the given user ID
     * @param userId User ID to check
     * @return true if the current user matches, false otherwise
     */
    public boolean isCurrentUser(Long userId) {
        try {
            return getCurrentUserId().equals(userId);
        } catch (AuthUnauthorizedException e) {
            return false;
        }
    }
}

