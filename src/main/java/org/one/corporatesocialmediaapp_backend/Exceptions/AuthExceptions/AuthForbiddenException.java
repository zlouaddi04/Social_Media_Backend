package org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions;

public class AuthForbiddenException extends RuntimeException {
    public AuthForbiddenException(String message) {
        super(message);
    }
}

