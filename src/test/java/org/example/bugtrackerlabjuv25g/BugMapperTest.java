package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}