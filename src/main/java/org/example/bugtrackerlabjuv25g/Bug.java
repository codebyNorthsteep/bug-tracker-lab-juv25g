package org.example.bugtrackerlabjuv25g;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "bug_date")
    private LocalDateTime bugDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "development_area")
    private DevelopmentArea developerArea;

    public Bug() {
    }

    public DevelopmentArea getDeveloperArea() {
        return developerArea;
    }

    public void setDeveloperArea(DevelopmentArea developerArea) {
        this.developerArea = developerArea;
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
