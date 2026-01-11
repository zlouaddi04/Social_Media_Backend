package org.one.corporatesocialmediaapp_backend.Exceptions;


import org.hibernate.exception.ConstraintViolationException;
import org.one.corporatesocialmediaapp_backend.DTO.ErrorResponse;
import org.one.corporatesocialmediaapp_backend.Enums.ErrorCodes;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentContentEmptyException;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentDeleteNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeAlreadyExistsException;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.PostContentEmptyException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.PostDeleteNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.PostNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.PostUpdateNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.ImageUploadException;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.InvalidImageException;
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
