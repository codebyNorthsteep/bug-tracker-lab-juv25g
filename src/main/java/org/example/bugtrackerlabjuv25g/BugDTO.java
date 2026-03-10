package org.example.bugtrackerlabjuv25g;

import java.time.LocalDateTime;

public record BugDTO(Long id,
                     String title,
                     String description,
                     String bugDate,
                     Priority priority,
                     DevelopmentArea developerArea) {
}
