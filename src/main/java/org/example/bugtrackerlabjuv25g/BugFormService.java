package org.example.bugtrackerlabjuv25g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BugFormService {
    @Autowired
    BugRepository bugRepository;

    public void saveReport(Bug bugFrom){
        bugRepository.save(bugFrom);
    }

    public Optional<Bug> getReport(long id){
        return bugRepository.findById(id);
    }

    public List<Bug> getAllBugs(){
        return bugRepository.findAll();
    }

}
