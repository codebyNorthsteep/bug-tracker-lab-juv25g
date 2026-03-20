package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BugFormServiceTest {

    @Mock
    BugRepository repository;
    @Mock
    BugMapper mapper;
    @InjectMocks
    BugFormService service;

    @Test
    void saveReportWithNullAndDuplicateTitleThrowsException() {
        CreateBugDTO bug = new CreateBugDTO("Test title", "Some Description", Priority.LOW, Development.BACKEND);
        var nullException = assertThrows(IllegalArgumentException.class, () ->
                service.saveReport(null));
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopment("Test title", Development.BACKEND)).thenReturn(true);
        var dupeException = assertThrows(IllegalArgumentException.class, () ->
                service.saveReport(bug));

        assertThat(nullException).hasMessage("bugForm must not be null");
        assertThat(dupeException).hasMessageContaining("A bug with this title already exists in development area:");
    }

    @Test
    void saveReportWithSimulatedDataViolationThrowsException() {
        CreateBugDTO bug = new CreateBugDTO("Test title", "Some Description", Priority.LOW, Development.BACKEND);
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopment("Test title", Development.BACKEND)).thenReturn(false);
        Mockito.when(repository.save(mapper.toEntity(bug))).thenThrow(DataIntegrityViolationException.class);
        var dataViolation = assertThrows(IllegalArgumentException.class, () ->
                service.saveReport(bug));
        assertThat(dataViolation).hasMessageContaining("Database integrity error: This bug was likely just reported by someone else.");
    }

    @Test
    void updateReport() {
    }

    @Test
    void deleteReport() {
    }

    @Test
    void getReport() {
    }

    @Test
    void getSearchByTitleOrDescription() {
    }

    @Test
    void getAllBugs() {
    }

    @Test
    void getPagedBugs() {
    }

    @Test
    void getCount() {
    }

    @Test
    void getBugsByPriority() {
    }

    @Test
    void getBugsByDevelopment() {
    }

    @Test
    void getAllBugsSortedByDate() {
    }

    @Test
    void getAllBugsSortedByPriority() {
    }
}
