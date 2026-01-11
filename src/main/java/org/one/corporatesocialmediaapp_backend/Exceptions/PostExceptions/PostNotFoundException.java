package org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
