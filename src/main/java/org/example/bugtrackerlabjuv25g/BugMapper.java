package org.example.bugtrackerlabjuv25g;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BugMapper {
    private static final Logger logger = LoggerFactory.getLogger(BugMapper.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    //Convert CreateBugDTO to BugEntity
    public Bug toEntity(CreateBugDTO createBugDTO) {
        logger.debug("Creating bug: title='{}', priority='{}', devArea='{}'",
                createBugDTO.title(),
                createBugDTO.priority(),
                createBugDTO.development());

        Bug bug = new Bug();
        bug.setTitle(createBugDTO.title());
        bug.setDescription(createBugDTO.description());
        bug.setPriority(createBugDTO.priority());
        bug.setDevelopment(createBugDTO.development());
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
                bug.getDevelopment()
        );
    }

    public void updateBug(UpdateBugDTO updateBugDTO, Bug bug) {
        bug.setTitle(updateBugDTO.title());
        bug.setDescription(updateBugDTO.description());
        bug.setPriority(updateBugDTO.priority());
        bug.setDevelopment(updateBugDTO.development());
    }
}
