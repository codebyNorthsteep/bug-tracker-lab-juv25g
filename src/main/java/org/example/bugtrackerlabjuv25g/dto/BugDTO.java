package org.example.bugtrackerlabjuv25g.dto;

import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;

/**
 * Data Transfer Object (DTO) representing a software development bug.
 * <p>
 * This class encapsulates bug-related details such as its unique identifier,
 * title, description, date of occurrence, priority level, and the associated
 * development area. It is used to transfer data between different layers of the
 * application while ensuring immutability.
 * <p>
 * Attributes:
 * - id: The unique identifier of the bug.
 * - title: A brief description or summary of the bug.
 * - description: A detailed explanation of the bug.
 * - bugDate: The date and time when the bug was created or reported, formatted as a string.
 * - priority: The priority level of the bug, indicating its severity or urgency.
 * - development: The area of development (e.g., FRONTEND or BACKEND) affected by the bug.
 */
public record BugDTO(Long id,
                     String title,
                     String description,
                     String bugDate,
                     Priority priority,
                     Development development) {
}
