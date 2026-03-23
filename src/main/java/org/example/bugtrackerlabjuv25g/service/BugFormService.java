package org.example.bugtrackerlabjuv25g.service;

import org.example.bugtrackerlabjuv25g.*;
import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugFormService {

    private final BugRepository bugRepository;
    private final BugMapper mapper;

    public BugFormService(BugRepository bugRepository, BugMapper mapper) {
        this.bugRepository = bugRepository;
        this.mapper = mapper;
    }

    private List<BugDTO> mapList(List<Bug> bugs) {
        return bugs.stream().map(mapper::toDTO).toList();
    }

    private Page<BugDTO> mapPage(Page<Bug> bugs) {
        return bugs.map(mapper::toDTO);
    }

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
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Database integrity error: This bug was likely just reported by someone else.", ex);
        }
    }

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
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Database integrity error: This bug was likely just reported by someone else.", ex);
        }
    }

    public void deleteReport(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (!bugRepository.existsById(id)) {
            throw new ResourceNotFound("Cannot delete bug: id " + id + " does not exist");
        }

        bugRepository.deleteById(id);
    }

    public BugDTO getReport(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        return bugRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFound("Bug with id " + id + " not found"));
    }


    public Page<BugDTO> getSearchByTitleOrDescription(String input, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged() || pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size cannot be less, equal to zero or bigger than 100");
        }
        return mapPage(bugRepository.findDistinctByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(input, input, pageable));
    }

    public List<BugDTO> getAllBugs() {
        return mapList(bugRepository.findAll());
    }

    public Page<BugDTO> getPagedBugs(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged() || pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size cannot be less, equal to zero or bigger than 100");
        }
        return mapPage(bugRepository.findAll(pageable));
    }

    public long getCount() {
        return bugRepository.count();
    }

    public List<BugDTO> getBugsByPriority(Priority priority) {
        return mapList(bugRepository.findAllByPriority(priority));
    }

    public List<BugDTO> getBugsByDevelopment(Development development) {
        return mapList(bugRepository.findAllByDevelopment(development));
    }

    public List<BugDTO> getAllBugsSortedByDate() {
        return mapList(bugRepository.findAllByOrderByBugDateDesc());
    }

    public List<BugDTO> getAllBugsSortedByPriority() {
        return mapList(bugRepository.findAllByOrderByPriorityDesc());
    }

}
