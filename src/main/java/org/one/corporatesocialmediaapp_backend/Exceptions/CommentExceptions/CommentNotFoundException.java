package org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}
