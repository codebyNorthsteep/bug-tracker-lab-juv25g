package org.example.bugtrackerlabjuv25g.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Catch specific ResourceNotFound exception and return error page with message
    @ExceptionHandler(ResourceNotFound.class)
    public String handleResourceNotFound(ResourceNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        return "error";
    }

    //Catch IllegalArgumentException when not catched locally in Controller-Layer and return error page with message
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorTitle", "Invalid Input");
        return "error";
    }

    // "Catch-all" for any other exceptions that are not specifically handled, to prevent application crashes and provide a user-friendly error message
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        model.addAttribute("errorTitle", "Server Error");
        return "error";
    }
}
