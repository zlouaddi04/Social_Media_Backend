package org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions;

public class CommentDeleteNotAllowedException extends RuntimeException {
    public CommentDeleteNotAllowedException(String message) {
        super(message);
    }
}
