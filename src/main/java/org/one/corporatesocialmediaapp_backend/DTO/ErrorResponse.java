package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;

public record ErrorResponse (
        String message,
        ErrorCodes errorCode

) {

}
