package org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions;

public class PostDeleteNotAllowedException extends RuntimeException {
    public PostDeleteNotAllowedException(String message) {
        super(message);
    }
}
