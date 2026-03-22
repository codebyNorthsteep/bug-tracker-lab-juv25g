package org.example.bugtrackerlabjuv25g.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;

/**
 * Data Transfer Object (DTO) for creating a new bug entry.
 * <p>
 * This class represents the data required for registering a bug in the system.
 * It ensures validation of the provided attributes and encapsulates necessary
 * details such as the bug's title, description, priority, and the relevant
 * development area. It is designed to be immutable.
 */
public record CreateBugDTO(@NotBlank(message = "Title can not be empty")
                           @Size(min = 3, max = 100)
                           String title,

                           @NotBlank(message = "Description can not be empty")
                           @Size(max = 1000, message = "Description must be at most {max} characters")
                           String description,

                           @NotNull(message = "Priority must be specified")
                           Priority priority,

                           @NotNull(message = "Development area must be specified")
                           Development development) {

    /**
     * Default constructor for the CreateBugDTO class.
     * Initializes a new instance of the CreateBugDTO class with default values:
     * - An empty string as the title.
     * - An empty string as the description.
     * - A {@code null} value for the priority.
     * - A {@code null} value for the development area.
     *
     * Useful for scenarios that require instantiation with no predefined data.
     */
    public CreateBugDTO() {
        this("", "", null, null);
    }

}
