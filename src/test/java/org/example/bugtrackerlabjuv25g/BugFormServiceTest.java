package org.example.bugtrackerlabjuv25g;

import org.example.bugtrackerlabjuv25g.dto.BugDTO;
import org.example.bugtrackerlabjuv25g.dto.CreateBugDTO;
import org.example.bugtrackerlabjuv25g.dto.UpdateBugDTO;
import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.example.bugtrackerlabjuv25g.mapper.BugMapper;
import org.example.bugtrackerlabjuv25g.model.Bug;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.example.bugtrackerlabjuv25g.repository.BugRepository;
import org.example.bugtrackerlabjuv25g.service.BugFormService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit tests for the BugFormService class.
 * This test class ensures the BugFormService methods function correctly
 * under various conditions, including invalid inputs, database errors, and happy paths.
 * Mocked dependencies of the BugFormService include BugRepository and BugMapper.
 * <p>
 * The following behaviors are tested:
 * - Validation checks for null or invalid input parameters in methods such as saveReport, updateReport,
 * deleteReport, getReport, and getSearchByTitleOrDescription.
 * - Handling of duplicate titles while saving or updating bugs.
 * - Exception handling when a database integrity error occurs during save and update operations.
 * - Correct behavior of delete and get operations, including exception scenarios when a resource is not found.
 * - Pagination validation in search operations to ensure proper limits on page size.
 * - Mapping between DTOs and entity objects using BugMapper.
 * <p>
 * This test class employs JUnit 5 as the testing framework along with Mockito for dependency mocking.
 * <p>
 * Annotations used:
 * - @ExtendWith: Integrates Mockito into the testing framework.
 * - @Mock: Creates mock instances of dependencies for testing.
 * - @InjectMocks: Automatically injects mocked dependencies into the service under test.
 * - @Test: Marks individual test methods.
 * - @DisplayName: Provides a custom description for test methods to improve readability of test reports.
 */
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
        Bug mapped = new Bug();
        Mockito.when(mapper.toEntity(bug)).thenReturn(mapped);
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopment("Test title", Development.BACKEND)).thenReturn(false);
        Mockito.when(repository.save(mapped)).thenThrow(DataIntegrityViolationException.class);
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
        Mockito.when(repository.existsByTitleIgnoreCaseAndDevelopmentAndIdNot(any(), any(), any())).thenReturn(true);


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
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        var resourceException = assertThrows(ResourceNotFound.class, () ->
                service.deleteReport(1L));

        assertThat(idNull).hasMessage("id must be greater than 0");
        assertThat(idLessZero).hasMessage("id must be greater than 0");
        assertThat(resourceException).hasMessageContaining("Cannot delete bug: id");
    }

    @Test
    @DisplayName("Delete report does not throw exception with valid input")
    void deleteReportWithValidInput() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(new Bug()));
        assertDoesNotThrow(() ->
                service.deleteReport(1L));
    }

    @Test
    @DisplayName("GetReport with invalid input throws exceptions")
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
    @DisplayName("GetReport with simulated valid input does not throw exception")
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
    @DisplayName("getSearchBy... with pageable result with invalid page size throws exception")
    void getSearchByTitleOrDescription() {
        Page<Bug> bugPage = new PageImpl<>(List.of(new Bug()));
        Mockito.when(repository.findDistinctByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Mockito.any(String.class), any(String.class), any(Pageable.class))).thenReturn(bugPage);
        Pageable validPage = PageRequest.of(0, 10);
        Pageable invalidHighPaged = PageRequest.of(0, 101);
        var highException = assertThrows(IllegalArgumentException.class, () ->
                service.getSearchByTitleOrDescription("test", invalidHighPaged));
        var unPagedException = assertThrows(IllegalArgumentException.class, () ->
                service.getSearchByTitleOrDescription("test", Pageable.unpaged()));
        var nullPage = assertThrows(IllegalArgumentException.class, () ->
                service.getSearchByTitleOrDescription("test", null));
        assertDoesNotThrow(() ->
                service.getSearchByTitleOrDescription("test", validPage));

        assertThat(highException).hasMessage("Page size cannot be less, equal to zero or bigger than 100");
        assertThat(unPagedException).hasMessage("Page size cannot be less, equal to zero or bigger than 100");
        assertThat(nullPage).hasMessage("Page size cannot be less, equal to zero or bigger than 100");
    }

    @Test
    @DisplayName("getPagedBugs with invalid page size throws exception")
    void getPagedBugs() {
        Page<Bug> bugPage = new PageImpl<>(List.of(new Bug()));
        Mockito.when(repository.findAll(any(Pageable.class))).thenReturn(bugPage);
        Pageable validPage = PageRequest.of(0, 10);
        Pageable invalidHighPaged = PageRequest.of(0, 101);
        var highException = assertThrows(IllegalArgumentException.class, () ->
                service.getPagedBugs(invalidHighPaged));
        var unPagedException = assertThrows(IllegalArgumentException.class, () ->
                service.getPagedBugs(Pageable.unpaged()));
        var nullPage = assertThrows(IllegalArgumentException.class, () ->
                service.getPagedBugs(null));
        assertDoesNotThrow(() ->
                service.getPagedBugs(validPage));

        assertThat(highException).hasMessage("Page size cannot be less, equal to zero or bigger than 100");
        assertThat(unPagedException).hasMessage("Page size cannot be less, equal to zero or bigger than 100");
        assertThat(nullPage).hasMessage("Page size cannot be less, equal to zero or bigger than 100");

    }

}
