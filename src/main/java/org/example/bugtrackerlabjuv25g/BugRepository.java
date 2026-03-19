package org.example.bugtrackerlabjuv25g;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

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
                WHEN org.example.bugtrackerlabjuv25g.Priority.HIGH THEN 0
                WHEN org.example.bugtrackerlabjuv25g.Priority.MEDIUM THEN 1
                WHEN org.example.bugtrackerlabjuv25g.Priority.LOW THEN 2
            END
            """)
    List<Bug> findAllByOrderByPriorityDesc();

    List<Bug> findBugsByTitleContainingIgnoreCase(String title);

    List<Bug> findBugsByDescriptionContainingIgnoreCase(String description);

    Page<Bug> findAll(Pageable pageable);

}
