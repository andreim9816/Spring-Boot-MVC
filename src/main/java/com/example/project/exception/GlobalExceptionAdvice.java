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

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handle(ForbiddenException e) {
        return new ModelAndView("access_denied");
    }
}
