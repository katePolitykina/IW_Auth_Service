package org.example.iw_authentication_service.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<String> handleKeycloakException(KeycloakException ex) {
        return ResponseEntity
                .status(HttpStatus.FAILED_DEPENDENCY)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String> handleUserServiceException(UserServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.FAILED_DEPENDENCY)
                .body(ex.getMessage());
    }

}
