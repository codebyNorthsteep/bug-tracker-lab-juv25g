package org.example.bugtrackerlabjuv25g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BugFormService {
    BugRepository bugRepository;
    BugMapper mapper;

    public BugFormService(BugRepository bugRepository, BugMapper bugMapper){
        this.bugRepository = bugRepository;
        this.mapper = bugMapper;
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

}
