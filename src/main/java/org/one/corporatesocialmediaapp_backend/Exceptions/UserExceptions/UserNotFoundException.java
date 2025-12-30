package org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
