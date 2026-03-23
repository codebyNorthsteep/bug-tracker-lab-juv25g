package org.example.bugtrackerlabjuv25g;

import org.example.bugtrackerlabjuv25g.dto.BugDTO;
import org.example.bugtrackerlabjuv25g.dto.CreateBugDTO;
import org.example.bugtrackerlabjuv25g.dto.UpdateBugDTO;
import org.example.bugtrackerlabjuv25g.mapper.BugMapper;
import org.example.bugtrackerlabjuv25g.model.Bug;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the BugMapper class, which validates the correct mapping
 * between DTOs and the Bug entity.
 * <p>
 * Responsibilities:
 * - Verifying that a CreateBugDTO is correctly mapped to a Bug entity.
 * - Verifying that a Bug entity is correctly mapped to a BugDTO.
 * - Ensuring proper handling of edge cases like null values during the mapping process.
 * - Validating the functionality of updating a Bug entity with an UpdateBugDTO.
 * <p>
 * Test Methods:
 * - {@code toEntity}: Tests mapping from CreateBugDTO to Bug entity.
 * - {@code toDTO}: Tests mapping from Bug entity to BugDTO.
 * - {@code toDTO_withNullBugDate}: Tests handling of null bugDate when mapping to BugDTO.
 * - {@code updateBug}: Tests updating a Bug entity with an UpdateBugDTO.
 * <p>
 * Annotations:
 * - {@code @BeforeEach}: Sets up the required BugMapper instance before each test method.
 * - {@code @Test}: Marks methods as test cases.
 * - {@code @DisplayName}: Describes the purpose of a test case for better readability.
 */
class BugMapperTest {

    private BugMapper bugMapper;

    @BeforeEach
    void setUp() {
        bugMapper = new BugMapper();
    }

    @Test
    @DisplayName("Should map CreateDTO to BugEntity correctly")
    void toEntity() {
        CreateBugDTO testBug = new CreateBugDTO("Test bug", "This is a test bug", Priority.LOW, Development.BACKEND);

        Bug result = bugMapper.toEntity(testBug);

        assertEquals(testBug.title(), result.getTitle());
        assertEquals(testBug.description(), result.getDescription());
        assertEquals(testBug.priority(), result.getPriority());
        assertEquals(testBug.development(), result.getDevelopment());

        assertNotNull(result.getBugDate(), "Bug date should be set to current date and time");
    }


    @Test
    @DisplayName("Should map BugEntity to BugDTO correctly")
    void toDTO() {
        Bug bug = new Bug();
        bug.setId(1L);
        bug.setTitle("Test bug");
        bug.setDescription("This is a test bug");
        bug.setPriority(Priority.HIGH);
        bug.setDevelopment(Development.FRONTEND);
        bug.setBugDate(LocalDateTime.of(2026, 3, 20, 10, 30, 0));

        BugDTO result = bugMapper.toDTO(bug);

        assertEquals(bug.getId(), result.id());
        assertEquals(bug.getTitle(), result.title());
        assertEquals(bug.getDescription(), result.description());
        assertEquals("2026-03-20 10:30:00", result.bugDate());
        assertEquals(bug.getPriority(), result.priority());
        assertEquals(bug.getDevelopment(), result.development());
    }

    @Test
    @DisplayName("Should handle null bugDate when mapping to BugDTO")
    void toDTO_withNullBugDate() {
        Bug bug = new Bug();
        bug.setId(1L);
        bug.setTitle("Test bug");
        bug.setDescription("This is a test bug");
        bug.setPriority(Priority.HIGH);
        bug.setDevelopment(Development.FRONTEND);
        bug.setBugDate(null); // Simulate null bugDate

        BugDTO result = bugMapper.toDTO(bug);

        assertEquals(bug.getId(), result.id());
        assertEquals(bug.getTitle(), result.title());
        assertEquals(bug.getDescription(), result.description());
        assertNull(result.bugDate(), "bugDate should be null when Bug's bugDate is null");
        assertEquals(bug.getPriority(), result.priority());
        assertEquals(bug.getDevelopment(), result.development());
    }

    @Test
    @DisplayName("Should update BugEntity with UpdateBugDTO correctly")
    void updateBug() {
        Bug existingBug = new Bug();
        existingBug.setTitle("Old title");
        existingBug.setDescription("Old description");
        existingBug.setPriority(Priority.MEDIUM);
        existingBug.setDevelopment(Development.BACKEND);

        UpdateBugDTO updateDTO = new UpdateBugDTO(1L, "New Title", "New Desc", Priority.HIGH, Development.BACKEND);

        bugMapper.updateBug(updateDTO, existingBug);

        assertEquals(updateDTO.title(), existingBug.getTitle());
        assertEquals(updateDTO.description(), existingBug.getDescription());
        assertEquals(updateDTO.priority(), existingBug.getPriority());
        assertEquals(updateDTO.development(), existingBug.getDevelopment());
    }
}