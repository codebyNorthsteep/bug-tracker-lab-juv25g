package org.example.bugtrackerlabjuv25g.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

/**
 * Represents a software development bug for tracking purposes.
 * <p>
 * This class is annotated as an entity, indicating it is mapped to a database table.
 * It defines attributes to capture essential bug details such as title, description,
 * date of occurrence, priority level, and the related development area.
 * Additional constraints are applied to ensure data integrity and validation.
 */
@Entity
// Database-level constraint to prevent duplicate bug-title in the same development area.
@Table(name = "bugs", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "development"}, name = "duplicated_bug_title_dev")})
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title can not be empty")
    @Size(min = 3, max = 100, message = "Title must be in between {min} and {max} characters")
    private String title;

    @NotBlank(message = "Description can not be empty")
    @Size(max = 1000, message = "Description must be at most {max} characters")
    @Column(length = 1000) // More space for text in database
    private String description;

    @NotNull(message = "Date must be provided")
    @PastOrPresent(message = "Date can not be in the future")
    @Column(name = "bug_date")
    private LocalDateTime bugDate;

    @NotNull(message = "Priority must be chosen")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    //Make PageRequest sort by priority 0 -> 2 instead of alphabetical high,medium, low
    @Formula("CASE priority WHEN 'HIGH' THEN 0 WHEN 'MEDIUM' THEN 1 ELSE 2 END")
    private int priorityOrder;

    @NotNull(message = "Development area must be chosen")
    @Enumerated(EnumType.STRING)
    private Development development;

    public Bug() {
    }

    public Development getDevelopment() {
        return development;
    }

    public void setDevelopment(Development development) {
        this.development = development;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getBugDate() {
        return bugDate;
    }

    public void setBugDate(LocalDateTime bugDate) {
        this.bugDate = bugDate;
    }

}
