package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

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
    void updateReportInputValidation() {
        UpdateBugDTO validUpdate = new UpdateBugDTO(2L, "Test Title", "Some Description", Priority.LOW, Development.BACKEND);
        UpdateBugDTO inValidUpdate = new UpdateBugDTO(null, "test title", "description", Priority.LOW, Development.BACKEND);

        var nullException = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(1L, null));
        var lessZero = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(-1L, validUpdate));
        var nonMatchId = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(1L, validUpdate));
        var nullId = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(2L, inValidUpdate));

        assertThat(nullException).hasMessage("updateDTO must not be null");
        assertThat(lessZero).hasMessage("id must be greater than 0");
        assertThat(nonMatchId).hasMessageContaining("Path id (1) and payload id (2)");
        assertThat(nullId).hasMessageContaining("Path id (2) and payload id");
    }

    @Test
    void updateReportExsistWithTitle() {
        UpdateBugDTO validUpdate = new UpdateBugDTO(2L, "Test Title", "Some Description", Priority.LOW, Development.BACKEND);
        Bug oldBug = new Bug();
        oldBug.setId(2L);
        oldBug.setTitle("Test Title");
        oldBug.setDevelopment(Development.BACKEND);
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(oldBug));
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopmentAndIdNot(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);


        var titleExist = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(2L, validUpdate));

        assertThat(titleExist).hasMessageContaining("A bug with this title already exists in");
    }

    @Test
    void updateReportMapperAndData() {
        UpdateBugDTO validUpdate = new UpdateBugDTO(2L, "Test Title", "Some Description", Priority.LOW, Development.BACKEND);
        Bug oldBug = new Bug();
        oldBug.setId(2L);
        oldBug.setTitle("Test Title");
        oldBug.setDevelopment(Development.BACKEND);
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(oldBug));
        Mockito.when(repository.save(oldBug)).thenThrow(DataIntegrityViolationException.class);

        var dataViolation = assertThrows(IllegalArgumentException.class, () ->
                service.updateReport(2L, validUpdate));

        assertThat(dataViolation).hasMessageContaining("Database integrity error: This bug was likely just reported");
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
