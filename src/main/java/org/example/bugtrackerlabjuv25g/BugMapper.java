package org.example.bugtrackerlabjuv25g;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BugMapper {
    //Convert CreateBugDTO to BugEntity
    public Bug toEntity(CreateBugDTO createBugDTO) {
        Bug bug = new Bug();
        bug.setTitle(createBugDTO.title());
        bug.setDescription(createBugDTO.description());
        bug.setPriority(createBugDTO.priority());
        bug.setDeveloperArea(createBugDTO.developerArea());
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
