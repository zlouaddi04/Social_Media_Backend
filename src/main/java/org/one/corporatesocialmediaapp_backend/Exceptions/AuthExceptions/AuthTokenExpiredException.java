package org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions;

public class AuthTokenExpiredException extends RuntimeException {
    public AuthTokenExpiredException(String message) {
        super(message);
    }
}

