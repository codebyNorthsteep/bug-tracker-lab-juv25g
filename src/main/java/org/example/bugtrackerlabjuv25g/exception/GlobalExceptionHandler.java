package org.example.bugtrackerlabjuv25g.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public String handleResourceNotFound(ResourceNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
