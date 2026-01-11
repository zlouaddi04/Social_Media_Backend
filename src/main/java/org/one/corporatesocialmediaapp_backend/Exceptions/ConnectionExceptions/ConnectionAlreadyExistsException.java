package org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions;

import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;

public class ConnectionAlreadyExistsException extends RuntimeException {
    private final ErrorCodes errorCode = ErrorCodes.FOLLOW_ALREADY_EXISTS;

    public ConnectionAlreadyExistsException(String message) {
        super(message);
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}

