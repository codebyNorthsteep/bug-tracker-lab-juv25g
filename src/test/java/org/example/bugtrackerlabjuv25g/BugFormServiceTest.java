package org.example.bugtrackerlabjuv25g;

import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    @DisplayName("SaveReport with invalid input should throw exceptions")
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
    @DisplayName("SaveReport with valid input but simulated database error throws exception")
    void saveReportWithSimulatedDataViolationThrowsException() {
        CreateBugDTO bug = new CreateBugDTO("Test title", "Some Description", Priority.LOW, Development.BACKEND);
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopment("Test title", Development.BACKEND)).thenReturn(false);
        Mockito.when(repository.save(mapper.toEntity(bug))).thenThrow(DataIntegrityViolationException.class);
        var dataViolation = assertThrows(IllegalArgumentException.class, () ->
                service.saveReport(bug));
        assertThat(dataViolation).hasMessageContaining("Database integrity error: This bug was likely just reported by someone else.");
    }

    @Test
    @DisplayName("UpdateReport with invalid input throws exception")
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
    @DisplayName("UpdateReport with duplicate title throws exception")
    void updateReportExistWithTitle() {
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
    @DisplayName("UpdateReport with simulated database error throws exception")
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
    @DisplayName("DeleteReport throws exception with invalid input, and does not throw with valid input")
    void deleteReport() {
        var idNull = assertThrows(IllegalArgumentException.class, () ->
                service.deleteReport(null));
        var idLessZero = assertThrows(IllegalArgumentException.class, () ->
                service.deleteReport(-1L));
        Mockito.when(repository.existsById(1L)).thenReturn(false);
        var resourceException = assertThrows(ResourceNotFound.class, () ->
                service.deleteReport(1L));


        Mockito.when(repository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() ->
                service.deleteReport(1L));

        assertThat(idNull).hasMessage("id must be greater than 0");
        assertThat(idLessZero).hasMessage("id must be greater than 0");
        assertThat(resourceException).hasMessageContaining("Cannot delete bug: id");
    }

    @Test
    @DisplayName("")
    void getReport() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setId(1L);

        var idNull = assertThrows(IllegalArgumentException.class, () ->
                service.getReport(null));
        var idLessZero = assertThrows(IllegalArgumentException.class, () ->
                service.getReport(-1L));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        var noResource = assertThrows(ResourceNotFound.class, () ->
                service.getReport(1L));

        assertThat(idNull).hasMessage("id must be greater than 0");
        assertThat(idLessZero).hasMessage("id must be greater than 0");
        assertThat(noResource).hasMessageContaining("Bug with id ");

    }

    @Test
    void getReportHappyPath() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setId(1L);
        BugDTO dto = new BugDTO(1L, "Test", "some", null, null, null);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(bug));
        Mockito.when(mapper.toDTO(bug)).thenReturn(dto);
        assertDoesNotThrow(() ->
                service.getReport(1L));
    }

    @Test
    void getSearchByTitleOrDescription() {
    }


    @Test
    void getPagedBugs() {
    }

    @Test
    void getBugsByPriority() {
    }

    @Test
    void getBugsByDevelopment() {
    }
}
