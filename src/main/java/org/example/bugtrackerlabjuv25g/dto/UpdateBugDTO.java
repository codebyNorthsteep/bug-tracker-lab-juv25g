package org.example.bugtrackerlabjuv25g.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;

/**
 * Data Transfer Object (DTO) used for updating an existing bug.
 * <p>
 * This record encapsulates the fields required to update a bug's details,
 * ensuring proper validation and constraints are applied to maintain data consistency.
 * It is primarily used for transferring update-related data between the client
 * and server layers.
 */
public record UpdateBugDTO(@NotNull
                           Long id,

                           @NotBlank(message = "Title can not be empty")
                           @Size(min = 3, max = 100)
                           String title,

                           @NotBlank(message = "Description can not be empty")
                           @Size(max = 1000, message = "Description must be at most {max} characters")
                           String description,

                           @NotNull(message = "Priority must be specified")
                           Priority priority,

                           @NotNull(message = "Development area must be specified")
                           Development development) {
}
