package org.one.corporatesocialmediaapp_backend.DTO;

import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ErrorResponse (
        String message,
        ErrorCodes errorCode,
        LocalTime timestamp

) {

}
