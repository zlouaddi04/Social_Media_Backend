package org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions;

public class UserUsernameAlreadyExists extends RuntimeException {
    public UserUsernameAlreadyExists(String message) {
        super(message);
    }
}
