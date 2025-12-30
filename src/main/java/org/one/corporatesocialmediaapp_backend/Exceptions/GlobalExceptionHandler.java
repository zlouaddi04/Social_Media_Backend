package org.one.corporatesocialmediaapp_backend.Exceptions;


import org.hibernate.exception.ConstraintViolationException;
import org.one.corporatesocialmediaapp_backend.DTO.ErrorResponse;
import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserEmailAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserUsernameAlreadyExists;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    // ==========USER_EXCEPTIONS==========

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse>  handleUserNotFoundException(UserNotFoundException ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse>  handleUserAlreadyExists(UserAlreadyExists ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.USER_ALREADY_EXISTS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Error);
    }

    @ExceptionHandler(UserEmailAlreadyExists.class)
    public ResponseEntity<ErrorResponse>  handleUserEmailAlreadyExists(UserEmailAlreadyExists ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.USER_EMAIL_ALREADY_EXISTS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Error);
    }

    @ExceptionHandler(UserUsernameAlreadyExists.class)
    public ResponseEntity<ErrorResponse>  handleUserUsernameAlreadyExists(UserUsernameAlreadyExists ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.USER_USERNAME_ALREADY_EXISTS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Error);
    }





    // ==========GENERAL_EXCEPTIONS==========

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.DB_VALIDATION_ERROR,
                LocalTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex) {
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.VALIDATION_ERROR,
                LocalTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex){
        ErrorResponse Error =new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.INTERNAL_SERVER_ERROR,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Error);
    }





}
