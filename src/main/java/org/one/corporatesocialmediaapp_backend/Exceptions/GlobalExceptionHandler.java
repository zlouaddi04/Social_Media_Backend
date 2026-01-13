package org.one.corporatesocialmediaapp_backend.Exceptions;


import org.hibernate.exception.ConstraintViolationException;
import org.one.corporatesocialmediaapp_backend.DTO.ErrorResponse;
import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;
import org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.ConnectionAlreadyExistsException;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.ConnectionNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.FollowSelfNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeAlreadyExistsException;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.ImageUploadException;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.InvalidImageException;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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


    // ==========AUTH/JWT_EXCEPTIONS==========

    @ExceptionHandler(AuthInvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthInvalidCredentialsException(AuthInvalidCredentialsException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.AUTH_INVALID_CREDENTIALS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }

    @ExceptionHandler(AuthUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleAuthUnauthorizedException(AuthUnauthorizedException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.AUTH_UNAUTHORIZED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }

    @ExceptionHandler(AuthForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleAuthForbiddenException(AuthForbiddenException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.AUTH_FORBIDDEN,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Error);
    }

    @ExceptionHandler(AuthTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleAuthTokenExpiredException(AuthTokenExpiredException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.AUTH_TOKEN_EXPIRED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }

    @ExceptionHandler(AuthTokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleAuthTokenInvalidException(AuthTokenInvalidException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.AUTH_TOKEN_INVALID,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }

    // Spring Security exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse Error = new ErrorResponse(
                "Invalid username or password",
                ErrorCodes.AUTH_INVALID_CREDENTIALS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.USER_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse Error = new ErrorResponse(
                "Authentication failed: " + ex.getMessage(),
                ErrorCodes.AUTH_UNAUTHORIZED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error);
    }


    // ==========POST_EXCEPTIONS==========

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.POST_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }

    @ExceptionHandler(PostContentEmptyException.class)
    public ResponseEntity<ErrorResponse> handlePostContentEmptyException(PostContentEmptyException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.POST_CONTENT_EMPTY,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
    }

    @ExceptionHandler(PostUpdateNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handlePostUpdateNotAllowedException(PostUpdateNotAllowedException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.POST_UPDATE_NOT_ALLOWED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Error);
    }

    @ExceptionHandler(PostDeleteNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handlePostDeleteNotAllowedException(PostDeleteNotAllowedException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.POST_DELETE_NOT_ALLOWED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Error);
    }


    // ==========COMMENT_EXCEPTIONS==========

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.COMMENT_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }

    @ExceptionHandler(CommentContentEmptyException.class)
    public ResponseEntity<ErrorResponse> handleCommentContentEmptyException(CommentContentEmptyException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.COMMENT_CONTENT_EMPTY,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
    }

    @ExceptionHandler(CommentDeleteNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleCommentDeleteNotAllowedException(CommentDeleteNotAllowedException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.COMMENT_DELETE_NOT_ALLOWED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Error);
    }


    // ==========LIKE_EXCEPTIONS==========

    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLikeAlreadyExistsException(LikeAlreadyExistsException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.LIKE_ALREADY_EXISTS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Error);
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLikeNotFoundException(LikeNotFoundException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.LIKE_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }


    // ==========CONNECTION_EXCEPTIONS==========

    @ExceptionHandler(ConnectionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConnectionAlreadyExistsException(ConnectionAlreadyExistsException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.FOLLOW_ALREADY_EXISTS,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Error);
    }

    @ExceptionHandler(ConnectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleConnectionNotFoundException(ConnectionNotFoundException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.RESOURCE_NOT_FOUND,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error);
    }

    @ExceptionHandler(FollowSelfNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleFollowSelfNotAllowedException(FollowSelfNotAllowedException ex) {
        ErrorResponse Error = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.FOLLOW_SELF_NOT_ALLOWED,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
    }


    // ==========STORAGE_EXCEPTIONS==========

    @ExceptionHandler(InvalidImageException.class)
    public  ResponseEntity<ErrorResponse>  handleInvalidImageException(InvalidImageException ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.INVALID_IMAGE,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);

    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorResponse>  handleImageUploadException(ImageUploadException ex){
        ErrorResponse Error=new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.IMAGE_UPLOAD_ERROR,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
    }





    // ==========GENERAL_EXCEPTIONS==========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse Error = new ErrorResponse(
                "Validation failed: " + errors,
                ErrorCodes.VALIDATION_ERROR,
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error);
    }

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
