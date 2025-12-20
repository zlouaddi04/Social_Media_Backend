package org.one.corporatesocialmediaapp_backend.DTO;

public record SuccessResponse<T>(
        String message,
        T data

){
}
