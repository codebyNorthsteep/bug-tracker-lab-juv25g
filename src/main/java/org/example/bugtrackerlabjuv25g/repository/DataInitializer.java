package org.example.bugtrackerlabjuv25g.repository;

import org.example.bugtrackerlabjuv25g.model.Bug;
import org.example.bugtrackerlabjuv25g.model.Development;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final BugRepository bugRepository;

    public DataInitializer(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create sample Bugs
        if (bugRepository.count() == 0) {

            bugRepository.saveAll(List.of(
                    createAndSaveBug("Null Pointer Exception login", "App crashes when clicking the button", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createAndSaveBug("CSS-error is wrong in header", "Text displays as purple", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(6)),
                    createAndSaveBug("UI Glitch", "Text overlaps on small screens", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(1)),
                    createAndSaveBug("Performance Issue", "App is slow when loading data", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(2)),
                    createAndSaveBug("Security Vulnerability", "SQL Injection possible in search", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createAndSaveBug("Memory Leak", "App uses more memory over time", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createAndSaveBug("Button not responsive", "Submit button doesn't respond to clicks", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(3)),
                    createAndSaveBug("Database connection timeout", "Connection drops after 5 minutes", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(4)),
                    createAndSaveBug("Login redirect issue", "User not redirected to dashboard after login", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(1)),
                    createAndSaveBug("Font rendering problem", "Fonts appear blurry on certain browsers", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(5)),
                    createAndSaveBug("API response delay", "API takes 10+ seconds to respond", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(2)),
                    createAndSaveBug("Form validation not working", "Email validation accepts invalid emails", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(7)),
                    createAndSaveBug("Image not loading", "Images fail to load on product page", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(3)),
                    createAndSaveBug("Cache clearing issue", "Cached data not updating properly", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(6)),
                    createAndSaveBug("Export function broken", "CSV export generates corrupted files", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(4)),
                    createAndSaveBug("Navigation menu alignment", "Menu items misaligned on mobile", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(8)),
                    createAndSaveBug("Search filter lag", "Search results delayed by 3+ seconds", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(2)))
            );

            logger.info("--- Demo-data has been created in database! ---");
        }
    }

    private Bug createAndSaveBug(String title, String description, Priority priority, Development development, LocalDateTime bugDate) {
        Bug bug = new Bug();
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setPriority(priority);
        bug.setDevelopment(development);
        bug.setBugDate(bugDate);
        return bug;
    }
}
