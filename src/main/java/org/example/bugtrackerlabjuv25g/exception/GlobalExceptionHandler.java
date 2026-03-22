package org.example.bugtrackerlabjuv25g.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The GlobalExceptionHandler class provides a centralized mechanism for handling exceptions
 * thrown across the application. It uses Spring's {@code @ControllerAdvice} to capture specific
 * exceptions and return appropriate HTTP status codes and error views for the user.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //Catch specific ResourceNotFound exception and return error page with message
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFound.class)
    public String handleResourceNotFound(ResourceNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        return "error";
    }

    //Catch IllegalArgumentException when not catched locally in Controller-Layer and return error page with message
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTitle", "Invalid Input");
        return "error";
    }

}
