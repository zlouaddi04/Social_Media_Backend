package org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions;

public class AuthTokenInvalidException extends RuntimeException {
    public AuthTokenInvalidException(String message) {
        super(message);
    }
}

