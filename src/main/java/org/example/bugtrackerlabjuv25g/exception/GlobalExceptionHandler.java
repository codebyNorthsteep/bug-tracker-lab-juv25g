package org.example.bugtrackerlabjuv25g.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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
