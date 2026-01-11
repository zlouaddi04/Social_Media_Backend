package org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions;

public class PostUpdateNotAllowedException extends RuntimeException {
    public PostUpdateNotAllowedException(String message) {
        super(message);
    }
}

