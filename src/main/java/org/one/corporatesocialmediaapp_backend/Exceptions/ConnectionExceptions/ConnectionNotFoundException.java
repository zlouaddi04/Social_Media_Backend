package org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions;

import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;

public class ConnectionNotFoundException extends RuntimeException {
    private final ErrorCodes errorCode = ErrorCodes.RESOURCE_NOT_FOUND;

    public ConnectionNotFoundException(String message) {
        super(message);
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}

