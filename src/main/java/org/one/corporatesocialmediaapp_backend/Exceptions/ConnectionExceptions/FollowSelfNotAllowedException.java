package org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions;

import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;

public class FollowSelfNotAllowedException extends RuntimeException {
    private final ErrorCodes errorCode = ErrorCodes.FOLLOW_SELF_NOT_ALLOWED;

    public FollowSelfNotAllowedException(String message) {
        super(message);
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}

