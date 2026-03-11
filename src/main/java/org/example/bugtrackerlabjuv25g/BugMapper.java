package org.example.bugtrackerlabjuv25g;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BugMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(BugMapper.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    //Convert CreateBugDTO to BugEntity
    public Bug toEntity(CreateBugDTO createBugDTO) {
        LOGGER.debug("Creating bug: title='{}', priority='{}', devArea='{}'",
                createBugDTO.title(),
                createBugDTO.priority(),
                createBugDTO.developerArea());

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
        String formattedDate = bug.getBugDate() != null
                ? bug.getBugDate().format(FORMATTER)
                : null;
        return new BugDTO(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                formattedDate,
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
