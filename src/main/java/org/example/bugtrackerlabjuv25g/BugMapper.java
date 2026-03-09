package org.example.bugtrackerlabjuv25g;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BugMapper {
    //Convert CreateBugDTO to BugEntity
    public Bug toEntity(CreateBugDTO createBugDTO) {
        Bug bug = new Bug();
        System.out.printf("""
                Title: %s
                Description: %s
                Prio: %s
                DevArea: %s
                """, createBugDTO.getTitle(), createBugDTO.getDescription(),
                createBugDTO.getPriority().toString(), createBugDTO.getDeveloperArea().toString());
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
