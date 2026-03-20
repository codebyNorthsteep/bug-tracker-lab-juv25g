package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class BugMapperTest {

    private BugMapper bugMapper;

    @BeforeEach
    void setUp() {
        bugMapper = new BugMapper();
    }

    @Test
    @DisplayName("Should map BugDTO to BugEntity correctly")
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

}