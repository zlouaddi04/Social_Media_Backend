package org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions;

public class AuthUnauthorizedException extends RuntimeException {
    public AuthUnauthorizedException(String message) {
        super(message);
    }
}

