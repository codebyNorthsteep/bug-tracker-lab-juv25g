package org.example.bugtrackerlabjuv25g;

public record BugDTO(Long id,
                     String title,
                     String description,
                     String bugDate,
                     Priority priority,
                     Development development) {
}
