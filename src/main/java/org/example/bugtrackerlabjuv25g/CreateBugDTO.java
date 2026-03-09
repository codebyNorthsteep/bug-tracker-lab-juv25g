package org.example.bugtrackerlabjuv25g;

import jakarta.validation.constraints.*;

public record CreateBugDTO(@NotBlank(message = "Title can not be empty")
                           @Size(min = 3, max = 100)
                           String title,

                           @NotBlank(message = "Description can not be empty")
                           @Size(max = 1000, message = "Description must be at most {max} characters")
                           String description,

                           @NotNull(message = "Priority must be specified")
                           Priority priority,

                           @NotNull(message = "Development area must be specified")
                           DevelopmentArea developerArea) {
}
