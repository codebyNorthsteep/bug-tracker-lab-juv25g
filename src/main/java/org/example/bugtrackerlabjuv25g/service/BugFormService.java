package org.example.bugtrackerlabjuv25g.service;

import org.example.bugtrackerlabjuv25g.dto.BugDTO;
import org.example.bugtrackerlabjuv25g.dto.CreateBugDTO;
import org.example.bugtrackerlabjuv25g.dto.UpdateBugDTO;
import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.example.bugtrackerlabjuv25g.mapper.BugMapper;
import org.example.bugtrackerlabjuv25g.model.Bug;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.example.bugtrackerlabjuv25g.repository.BugRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer responsible for bug tracking business logic.
 * Main responsibilities include:
 * Coordinating CRUD operations between the controller and {@link BugRepository}.
 * Enforcing business rules, such as unique titles per development area.
 * Handling data transformation between entities and DTOs via {@link BugMapper}.
 * Providing paginated and filtered results for optimized data retrieval.
 *
 */
@Service
public class BugFormService {

    private final BugRepository bugRepository;
    private final BugMapper mapper;

    public BugFormService(BugRepository bugRepository, BugMapper mapper) {
        this.bugRepository = bugRepository;
        this.mapper = mapper;
    }

    /**
     * Maps a list of {@link Bug} entities to a list of {@link BugDTO} data transfer objects.
     *
     * @param bugs the list of {@link Bug} entities to map
     * @return a list of {@link BugDTO} objects corresponding to the provided {@link Bug} entities
     */
    private List<BugDTO> mapList(List<Bug> bugs) {
        return bugs.stream().map(mapper::toDTO).toList();
    }

    /**
     * Maps a {@link Page} of {@link Bug} entities to a {@link Page} of {@link BugDTO} data transfer objects.
     *
     * @param bugs the {@link Page} of {@link Bug} entities to map
     * @return a {@link Page} of {@link BugDTO} objects corresponding to the provided {@link Bug} entities
     */
    private Page<BugDTO> mapPage(Page<Bug> bugs) {
        return bugs.map(mapper::toDTO);
    }

    /**
     * Saves a new bug report to the repository. Validates that the provided bug report
     * data is not null and checks for duplicates based on the title and development area.
     * Throws exceptions for validation errors or database integrity violations.
     *
     * @param bugForm the data transfer object representing the new bug report to save
     *                including title, description, priority, and other related details.
     * @throws IllegalArgumentException if the provided bugForm is null, or if a bug with
     *                                  the same title already exists in the specified
     *                                  development area, or if a database integrity
     *                                  violation occurs.
     */
    public void saveReport(CreateBugDTO bugForm) {
        if (bugForm == null) {
            throw new IllegalArgumentException("bugForm must not be null");
        }
        if (bugRepository.existsByTitleIgnoreCaseAndDevelopment(bugForm.title(), bugForm.development())) {
            throw new IllegalArgumentException(
                    "A bug with this title already exists in development area: " + bugForm.development()
            );
        }
        try {
            bugRepository.save(mapper.toEntity(bugForm));
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Database integrity error: This bug was likely just reported by someone else.", ex);
        }
    }

    /**
     * Updates an existing bug report identified by its ID with the information provided
     * in the UpdateBugDTO object. Ensures the ID in the path matches the ID in the data
     * transfer object, validates that no duplications exist based on the title and
     * development area, and handles database integrity issues during the save operation.
     *
     * @param existingId   the ID of the bug report to update; must be greater than 0
     * @param updateBugDTO the data transfer object containing the updated details of
     *                     the bug report, including title, description, and other fields
     * @throws IllegalArgumentException if the updateBugDTO is null, if the existingId is
     *                                  invalid, if the IDs in the path and payload do not
     *                                  match, if another bug with the same title exists
     *                                  in the same development area, or if there is a
     *                                  database integrity violation
     * @throws ResourceNotFound         if a bug with the specified ID does not exist in the repository
     */
    public void updateReport(long existingId, UpdateBugDTO updateBugDTO) {
        if (updateBugDTO == null) {
            throw new IllegalArgumentException("updateDTO must not be null");
        }
        if (existingId <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (updateBugDTO.id() == null || updateBugDTO.id() != existingId) {
            throw new IllegalArgumentException("Path id (" + existingId + ") and payload id (" + updateBugDTO.id() + ") must match");
        }
        Bug existingBug = bugRepository.findById(existingId).orElseThrow(() -> new ResourceNotFound("Bug with id " + existingId + " not found"));

        if (bugRepository.existsByTitleIgnoreCaseAndDevelopmentAndIdNot(
                updateBugDTO.title(), updateBugDTO.development(), existingId)) {
            throw new IllegalArgumentException(
                    "A bug with this title already exists in development area: " + updateBugDTO.development()
            );
        }

        mapper.updateBug(updateBugDTO, existingBug);
        try {
            bugRepository.save(existingBug);
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Database integrity error: This bug was likely just reported by someone else.", ex);
        }
    }

    /**
     * Deletes a bug report from the repository based on the provided ID.
     * Validates that the ID is greater than 0 and ensures that a bug with
     * the specified ID exists before attempting the deletion.
     *
     * @param id the unique identifier of the bug report to delete; must be greater than 0
     * @throws IllegalArgumentException if the provided ID is null or less than or equal to 0
     * @throws ResourceNotFound         if no bug report with the specified ID exists in the repository
     */
    public void deleteReport(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        Bug existingBug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Cannot delete bug: id " + id + " does not exist"));

        bugRepository.delete(existingBug);
    }

    /**
     * Retrieves a bug report based on the provided ID.
     *
     * @param id the unique identifier of the bug report; must be greater than 0
     * @return the bug report as a BugDTO object
     * @throws IllegalArgumentException if the provided id is null or less than or equal to 0
     * @throws ResourceNotFound         if no bug report is found with the given id
     */
    public BugDTO getReport(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        return bugRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFound("Bug with id " + id + " not found"));
    }


    /**
     * Searches for bugs by matching the given input string against the title or description fields,
     * performing a case-insensitive search. Results are paginated.
     *
     * @param input    The search keyword to look for in the title or description of bugs.
     * @param pageable The pagination information including page number, size, and sorting options.
     * @return A page of BugDTO objects that match the search criteria.
     */
    public Page<BugDTO> getSearchByTitleOrDescription(String input, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged() || pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size cannot be less, equal to zero or bigger than 100");
        }
        return mapPage(bugRepository.findDistinctByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(input, input, pageable));
    }

    /**
     * Retrieves a list of all bugs from the repository and maps them to BugDTO objects.
     *
     * @return a list of BugDTO containing details of all bugs.
     */
    public List<BugDTO> getAllBugs() {
        return mapList(bugRepository.findAll());
    }

    /**
     * Retrieves a paginated list of bugs and maps them to BugDTO objects.
     *
     * @param pageable the pagination and sorting information
     * @return a Page containing BugDTO objects
     */
    public Page<BugDTO> getPagedBugs(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged() || pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size cannot be less, equal to zero or bigger than 100");
        }
        return mapPage(bugRepository.findAll(pageable));
    }

    /**
     * Retrieves the total count of entities in the bug repository.
     *
     * @return the number of entities available in the bug repository as a long value
     */
    public long getCount() {
        return bugRepository.count();
    }

    /**
     * Retrieves a list of bugs filtered by the specified priority.
     *
     * @param priority the priority level used to filter the bugs
     * @return a list of BugDTO objects corresponding to the bugs with the specified priority
     */
    public List<BugDTO> getBugsByPriority(Priority priority) {
        return mapList(bugRepository.findAllByPriority(priority));
    }

    /**
     * Retrieves a list of bugs associated with a specific development entity.
     *
     * @param development the development entity for which associated bugs are to be retrieved
     * @return a list of BugDTO objects representing the bugs linked to the provided development entity
     */
    public List<BugDTO> getBugsByDevelopment(Development development) {
        return mapList(bugRepository.findAllByDevelopment(development));
    }

    /**
     * Retrieves a list of all bugs sorted by their date in descending order.
     *
     * @return a list of BugDTO objects sorted by bug date in descending order
     */
    public List<BugDTO> getAllBugsSortedByDate() {
        return mapList(bugRepository.findAllByOrderByBugDateDesc());
    }

    /**
     * Retrieves all bugs from the repository and returns them sorted in descending order of priority.
     *
     * @return a list of BugDTO objects sorted by priority in descending order
     */
    public List<BugDTO> getAllBugsSortedByPriority() {
        return mapList(bugRepository.findAllByOrderByPriorityDesc());
    }

}
