package org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions;

public class CommentContentEmptyException extends RuntimeException {
    public CommentContentEmptyException(String message) {
        super(message);
    }
}
