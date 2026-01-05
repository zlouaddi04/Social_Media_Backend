package org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException(String message) {
        super(message);
    }
}
