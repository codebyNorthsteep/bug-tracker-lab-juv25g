package org.example.bugtrackerlabjuv25g.repository;

import org.example.bugtrackerlabjuv25g.model.Bug;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * Repository interface for managing Bug entities in the database.
 * <p>
 * This interface provides methods for standard CRUD operations as well as
 * custom query methods for handling specific requirements related to Bug data.
 * The interface extends ListCrudRepository, which provides generic CRUD functionality.
 */
public interface BugRepository extends ListCrudRepository<Bug, Long> {

    boolean existsByTitleIgnoreCaseAndDevelopment(String title, Development development);

    boolean existsByTitleIgnoreCaseAndDevelopmentAndIdNot(String title, Development development, Long id);

    List<Bug> findAllByDevelopment(Development development);

    List<Bug> findAllByPriority(Priority priority);

    List<Bug> findAllByOrderByBugDateDesc();

    @Query("""
            SELECT b
            FROM Bug b
            ORDER BY CASE b.priority
                WHEN org.example.bugtrackerlabjuv25g.model.Priority.HIGH THEN 0
                WHEN org.example.bugtrackerlabjuv25g.model.Priority.MEDIUM THEN 1
                WHEN org.example.bugtrackerlabjuv25g.model.Priority.LOW THEN 2
            END
            """)
    List<Bug> findAllByOrderByPriorityDesc();

    List<Bug> findBugsByTitleContainingIgnoreCase(String title);

    List<Bug> findBugsByDescriptionContainingIgnoreCase(String description);

    Page<Bug> findAll(Pageable pageable);

    Page<Bug> findDistinctByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description,
            Pageable pageable
    );
}
