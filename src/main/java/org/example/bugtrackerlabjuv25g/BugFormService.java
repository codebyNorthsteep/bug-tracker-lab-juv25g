package org.example.bugtrackerlabjuv25g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BugFormService {
    BugRepository bugRepository;
    BugMapper mapper;

    //Dependencyinjekta även Mapper, låter Spring injekta istället Mapper är en component
    public BugFormService(BugRepository bugRepository, BugMapper mapper) {
        this.bugRepository = bugRepository;
        this.mapper = mapper;
    }

    public void saveReport(CreateBugDTO bugForm){
        bugRepository.save(mapper.toEntity(bugForm));
    }

    public Optional<Bug> getReport(long id){
        return bugRepository.findById(id);
    }

    public List<BugDTO> getAllBugs(){
        return bugRepository.findAll().stream().map(mapper::toDTO)
                .toList();
    }
    public long getCount(){
        return bugRepository.count();
    }

    public long getHighPrioBugs(){
        return bugRepository.findAll().stream().filter(
                bug -> bug.getPriority().equals(Priority.HIGH)
        ).count();
    }

}
