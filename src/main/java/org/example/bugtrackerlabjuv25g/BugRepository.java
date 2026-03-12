package org.example.bugtrackerlabjuv25g;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BugRepository extends ListCrudRepository<Bug, Long> {

    List<Bug> findAllByDeveloperArea(DevelopmentArea developerArea);

    List<Bug> findAllByPriority(Priority priority);

    List<Bug> findAllByOrderByBugDateDesc();

    List<Bug> findAllByOrderByPriorityDesc();
}
