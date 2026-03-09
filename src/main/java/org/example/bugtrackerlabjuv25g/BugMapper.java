package org.example.bugtrackerlabjuv25g;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BugMapper {
    private static final Logger logger = LoggerFactory.getLogger(BugMapper.class);


    //Convert CreateBugDTO to BugEntity
    public Bug toEntity(CreateBugDTO createBugDTO) {
        logger.debug("Creating bug: title='{}', priority='{}', devArea='{}'",
                createBugDTO.getTitle(),
                createBugDTO.getPriority(),
                createBugDTO.getDeveloperArea());

        Bug bug = new Bug();
        bug.setTitle(createBugDTO.getTitle());
        bug.setDescription(createBugDTO.getDescription());
        bug.setPriority(createBugDTO.getPriority());
        bug.setDeveloperArea(createBugDTO.getDeveloperArea());
        bug.setBugDate(LocalDateTime.now());

        return bug;
    }

    //From Entity to DTO for show
    public BugDTO toDTO(Bug bug) {
        return new BugDTO(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                bug.getBugDate(),
                bug.getPriority(),
                bug.getDeveloperArea()
        );
    }

    public void updateBug(UpdateBugDTO updateBugDTO, Bug bug) {
        bug.setTitle(updateBugDTO.title());
        bug.setDescription(updateBugDTO.description());
        bug.setPriority(updateBugDTO.priority());
        bug.setDeveloperArea(updateBugDTO.developerArea());
    }
}
