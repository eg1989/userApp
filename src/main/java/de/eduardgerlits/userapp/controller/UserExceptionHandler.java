package de.eduardgerlits.userapp.controller;

import de.eduardgerlits.userapp.api.ApiError;
import de.eduardgerlits.userapp.exception.ApiUnreachableException;
import de.eduardgerlits.userapp.exception.UserNotFoundException;
import de.eduardgerlits.userapp.exception.UserPostsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserPostsNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserPostsNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ApiUnreachableException.class)
    public ResponseEntity<ApiError> handleApiUnreachableException(ApiUnreachableException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
