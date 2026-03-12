package org.example.bugtrackerlabjuv25g;

import org.springframework.beans.factory.annotation.Autowired;
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
        bugRepository.save(mapper.toEntity(bugForm));
    }

    public Optional<Bug> getReport(long id){
        return bugRepository.findById(id);
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

    public List<BugDTO> getBugsByDeveloperArea(DevelopmentArea developmentArea){
        return mapList(bugRepository.findAllByDeveloperArea(developmentArea));
    }

    public List<BugDTO> getAllBugsSortedByDate() {
        return mapList(bugRepository.findAllByOrderByBugDateDesc());
    }

    public List<BugDTO> getAllBugsSortedByPriority() {
        return mapList(bugRepository.findAllByOrderByPriorityDesc());
    }
}
