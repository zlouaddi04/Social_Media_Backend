package org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions;

public class UserEmailAlreadyExists extends RuntimeException {
    public UserEmailAlreadyExists(String message) {
        super(message);
    }
}
