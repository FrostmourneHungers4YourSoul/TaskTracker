package com.example.tracker.exception.handler;

import com.example.tracker.exception.ResourceAlreadyExistsException;
import com.example.tracker.exception.ResourceNotFoundException;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateException(ResourceAlreadyExistsException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                exception.getMessage()
        );
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(ResourceNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not found",
                exception.getMessage()
        );
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentEx(IllegalArgumentException ex) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException ex) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(HibernateException ex) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
