package com.example.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionAdvice {

    private static final String DEFAULT_MESSAGE = "Something went wrong. Please try again later!";
    private static final String BAD_REQUEST_MESSAGE = "Invalid parameters!";

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handle(EntityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error_not_found");

        var error = ErrorBody.builder()
                .message(String.format("%s with ID %s doesn't exist!", e.getEntityType(), e.getEntityId()))
                .build();

        modelAndView.addObject("exception", error);
        return modelAndView;
    }

    //
//    /* PathVariab validation */
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<List<ErrorBody>> handle(ConstraintViolationException e) {
//        return new ResponseEntity<>(
//                e.getConstraintViolations().stream()
//                        .map(ex -> ErrorBody.builder()
//                                .message(ex.getMessage())
//                                .build()
//                        )
//                        .collect(Collectors.toList()),
//                HttpStatus.NOT_FOUND
//        );
//    }
//
//    /* ReqDto field validation */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<List<ErrorBody>> handle(MethodArgumentNotValidException e) {
//        return new ResponseEntity<>(
//                e.getBindingResult().getFieldErrors().stream()
//                        .map(ex -> ErrorBody.builder()
//                                .message(ex.getDefaultMessage())
//                                .build()
//                        )
//                        .collect(Collectors.toList()),
//                HttpStatus.BAD_REQUEST
//        );
//    }
//
//    @ExceptionHandler(CustomException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ModelAndView handle(CustomException e) {
//        ModelAndView modelAndView = new ModelAndView("error_default");
//
//        var error = ErrorBody.builder()
//                .message(e.getMessage())
//                .build();
//        //todo log error
//        modelAndView.addObject("exception", error);
//        return modelAndView;
//    }
//
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<ErrorBody> handle(ValidationException e) {
//        return new ResponseEntity<>(
//                ErrorBody.builder()
//                        .message(BAD_REQUEST_MESSAGE)
//                        .build(),
//                HttpStatus.BAD_REQUEST
//        );
//    }
//
//    /* Postman invalid params (eg passing null to path variables) */
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<ErrorBody> handle(MethodArgumentTypeMismatchException e) {
//        return new ResponseEntity<>(
//                ErrorBody.builder()
//                        .message(BAD_REQUEST_MESSAGE)
//                        .build(),
//                HttpStatus.BAD_REQUEST
//        );
//    }
//
//

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handle(Exception e) {
//        ModelAndView modelAndView = new ModelAndView("error_default");
//
//        var error = ErrorBody.builder()
//                .message(DEFAULT_MESSAGE)
//                .build();
//        //todo log error
//        modelAndView.addObject("exception", error);
//        return modelAndView;
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorBody> handle(Exception e) {
//        return new ResponseEntity<>(
//                ErrorBody.builder()
//                        .message(DEFAULT_MESSAGE)
//                        .build(),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }
}
