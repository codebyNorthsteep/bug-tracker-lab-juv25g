package org.example.bugtrackerlabjuv25g.mapper;

import org.example.bugtrackerlabjuv25g.dto.BugDTO;
import org.example.bugtrackerlabjuv25g.dto.CreateBugDTO;
import org.example.bugtrackerlabjuv25g.dto.UpdateBugDTO;
import org.example.bugtrackerlabjuv25g.model.Bug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides mapping functionalities to convert between bug-related data transfer objects (DTOs)
 * and the Bug entity. It is designed to facilitate the transformation of bug data across different
 * layers of the application, ensuring proper encapsulation and separation of concerns.
 * <p>
 * Responsibilities:
 * - Converting instances of CreateBugDTO to Bug entity objects for persistence in the database.
 * - Converting Bug entity objects to BugDTO instances for display or communication purposes.
 * - Updating an existing Bug entity based on the provided UpdateBugDTO.
 * <p>
 * Dependencies:
 * - Utilizes a logger to record debugging information during data transformation operations.
 * - Employs DateTimeFormatter to format date and time as strings for output purposes.
 */
@Component
public class BugMapper {
    private static final Logger logger = LoggerFactory.getLogger(BugMapper.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * Converts a {@code CreateBugDTO} instance into a {@code Bug} entity.
     *
     * @param createBugDTO the data transfer object containing the bug details to be converted
     * @return a {@code Bug} entity populated with the data from the given {@code CreateBugDTO}
     */
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

    /**
     * Converts a {@code Bug} entity into a {@code BugDTO}.
     * This method maps the attributes from a {@code Bug} entity to a {@code BugDTO}
     * for the purpose of transferring bug details between different layers of the
     * application. It formats the {@code bugDate} field if it is not null.
     *
     * @param bug the {@code Bug} entity to be converted
     * @return a {@code BugDTO} containing the corresponding details from the given {@code Bug} entity
     */
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

    /**
     * Updates the details of an existing {@code Bug} entity using the values from
     * the specified {@code UpdateBugDTO} data transfer object.
     *
     * @param updateBugDTO the data transfer object containing updated bug details,
     *                     including title, description, priority, and development area
     * @param bug          the {@code Bug} entity to be updated with values from {@code updateBugDTO}
     */
    public void updateBug(UpdateBugDTO updateBugDTO, Bug bug) {
        bug.setTitle(updateBugDTO.title());
        bug.setDescription(updateBugDTO.description());
        bug.setPriority(updateBugDTO.priority());
        bug.setDevelopment(updateBugDTO.development());
    }
}
