package com.example.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionAdvice {

    private static final String DEFAULT_MESSAGE = "Something went wrong. Please try again later!";
    private static final String BAD_REQUEST_MESSAGE = "Invalid parameters!";

    @ExceptionHandler(com.example.project.exception.EntityNotFoundException.class)
    public ResponseEntity<com.example.project.exception.ErrorBody> handle(com.example.project.exception.EntityNotFoundException e) {
        return new ResponseEntity<>(
                com.example.project.exception.ErrorBody.builder()
                        .message(String.format("%s with ID %s doesn't exist!", e.getEntityType(), e.getEntityId()))
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    /* PathVariab validation */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<com.example.project.exception.ErrorBody>> handle(ConstraintViolationException e) {
        return new ResponseEntity<>(
                e.getConstraintViolations().stream()
                        .map(ex -> com.example.project.exception.ErrorBody.builder()
                                .message(ex.getMessage())
                                .build()
                        )
                        .collect(Collectors.toList()),
                HttpStatus.NOT_FOUND
        );
    }

    /* ReqDto field validation */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<com.example.project.exception.ErrorBody>> handle(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                e.getBindingResult().getFieldErrors().stream()
                        .map(ex -> com.example.project.exception.ErrorBody.builder()
                                .message(ex.getDefaultMessage())
                                .build()
                        )
                        .collect(Collectors.toList()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<com.example.project.exception.ErrorBody> handle(CustomException e) {
        return new ResponseEntity<>(
                com.example.project.exception.ErrorBody.builder()
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<com.example.project.exception.ErrorBody> handle(ValidationException e) {
        return new ResponseEntity<>(
                com.example.project.exception.ErrorBody.builder()
                        .message(BAD_REQUEST_MESSAGE)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    /* Postman invalid params (eg passing null to path variables) */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<com.example.project.exception.ErrorBody> handle(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(
                com.example.project.exception.ErrorBody.builder()
                        .message(BAD_REQUEST_MESSAGE)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.example.project.exception.ErrorBody> handle(Exception e) {
        return new ResponseEntity<>(
                ErrorBody.builder()
                        .message(DEFAULT_MESSAGE)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
