package org.example.bugtrackerlabjuv25g;

import jakarta.validation.constraints.*;

public class CreateBugDTO {
    @NotBlank(message = "Title can not be empty")
    @Size(min = 3, max = 100)
    String title;

    @NotBlank(message = "Description can not be empty")
    @Size(max = 1000, message = "Description must be at most {max} characters")
    String description;

    @NotNull(message = "Priority must be specified")
    Priority priority;

    @NotNull(message = "Development area must be specified")
    DevelopmentArea developerArea;

    public CreateBugDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public DevelopmentArea getDeveloperArea() {
        return developerArea;
    }

    public void setDeveloperArea(DevelopmentArea developerArea) {
        this.developerArea = developerArea;
    }
}
