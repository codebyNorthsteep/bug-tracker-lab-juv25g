package org.example.bugtrackerlabjuv25g;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BugFormService {

    private final BugRepository bugRepository;
    private final BugMapper mapper;

    //Dependencyinjekta även Mapper, låter Spring injekta istället Mapper är en component
    public BugFormService(BugRepository bugRepository, BugMapper mapper) {
        this.bugRepository = bugRepository;
        this.mapper = mapper;
    }

    private List<BugDTO> mapList(List<Bug> bugs) {
        return bugs.stream().map(mapper::toDTO).toList();
    }

    public void saveReport(CreateBugDTO bugForm){
        if (bugForm == null) {
            throw new IllegalArgumentException("bugForm must not be null");
        }
        //Will throw another exception when GlobalExceptionHandler is usable
        if (bugRepository.existsByTitleIgnoreCaseAndDevelopment(bugForm.title(), bugForm.development())) {
            throw new IllegalArgumentException(
                    "A bug with this title already exists in development area: " + bugForm.development()
            );
        }
        try {
            bugRepository.save(mapper.toEntity(bugForm));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Database integrity error: This bug was likely just reported by someone else.", ex);
        }    }

    public void updateReport(long existingId, UpdateBugDTO updateBugDTO) {
        if (updateBugDTO == null) {
            throw new IllegalArgumentException("updateDTO must not be null");
        }
        if(existingId <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (updateBugDTO.id() != existingId) {
            throw new IllegalArgumentException("Cannot update bug: id " + existingId + " does not exist");
        }

        if (bugRepository.existsByTitleIgnoreCaseAndDevelopmentAndIdNot(
                updateBugDTO.title(), updateBugDTO.development(), existingId)) {
            throw new IllegalArgumentException(
                    "A bug with this title already exists in development area: " + updateBugDTO.development()
            );
        }

        //Will throw another exception when GlobalExceptionHandler is usable
        Bug existingBug = bugRepository.findById(existingId).orElseThrow(() -> new IllegalArgumentException("Bug with id " + existingId + " not found"));
        mapper.updateBug(updateBugDTO, existingBug);
        bugRepository.save(existingBug);
    }

    public void deleteReport(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (!bugRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete bug: id " + id + " does not exist");
        }

            bugRepository.deleteById(id);
    }

    public Optional<BugDTO> getReport(long id){
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        return bugRepository.findById(id).map(mapper::toDTO);
    }

    public List<BugDTO> getAllBugs(){
            return mapList(bugRepository.findAll());
    }
    public long getCount(){
            return bugRepository.count();
    }

    public List<BugDTO> getBugsByPriority(Priority priority){
            return mapList(bugRepository.findAllByPriority(priority));
    }

    public List<BugDTO> getBugsByDevelopment(Development development){
            return mapList(bugRepository.findAllByDevelopment(development));
    }

    public List<BugDTO> getAllBugsSortedByDate() {
            return mapList(bugRepository.findAllByOrderByBugDateDesc());
    }

    public List<BugDTO> getAllBugsSortedByPriority() {
            return mapList(bugRepository.findAllByOrderByPriorityDesc());
    }

}
