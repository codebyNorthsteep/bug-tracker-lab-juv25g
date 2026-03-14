package org.example.bugtrackerlabjuv25g;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

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
