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

/**
 * Initializes the application's data by creating and persisting sample bug reports into the database.
 * <p>
 * This class implements the {@code CommandLineRunner} interface to allow execution of specific logic
 * once the application context has loaded. Upon application startup, the {@code run} method verifies if
 * the underlying data store is empty and populates it with a predefined set of sample bugs.
 * <p>
 * The goal of this initializer is to provide a ready-to-use database with test data, aiding in
 * application testing and demonstration.
 * <p>
 * Note:
 * - The sample bugs created include different combinations of titles, descriptions, priorities,
 * development areas, and bug report dates for testing various application functionalities.
 */
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
                    createBug("Null Pointer Exception login", "App crashes when clicking the button", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createBug("CSS-error is wrong in header", "Text displays as purple", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(6)),
                    createBug("UI Glitch", "Text overlaps on small screens", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(1)),
                    createBug("Performance Issue", "App is slow when loading data", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(2)),
                    createBug("Security Vulnerability", "SQL Injection possible in search", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createBug("Memory Leak", "App uses more memory over time", Priority.HIGH, Development.BACKEND, LocalDateTime.now()),
                    createBug("Button not responsive", "Submit button doesn't respond to clicks", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(3)),
                    createBug("Database connection timeout", "Connection drops after 5 minutes", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(4)),
                    createBug("Login redirect issue", "User not redirected to dashboard after login", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(1)),
                    createBug("Font rendering problem", "Fonts appear blurry on certain browsers", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(5)),
                    createBug("API response delay", "API takes 10+ seconds to respond", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(2)),
                    createBug("Form validation not working", "Email validation accepts invalid emails", Priority.MEDIUM, Development.FRONTEND, LocalDateTime.now().minusDays(7)),
                    createBug("Image not loading", "Images fail to load on product page", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(3)),
                    createBug("Cache clearing issue", "Cached data not updating properly", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(6)),
                    createBug("Export function broken", "CSV export generates corrupted files", Priority.HIGH, Development.BACKEND, LocalDateTime.now().minusDays(4)),
                    createBug("Navigation menu alignment", "Menu items misaligned on mobile", Priority.LOW, Development.FRONTEND, LocalDateTime.now().minusDays(8)),
                    createBug("Search filter lag", "Search results delayed by 3+ seconds", Priority.MEDIUM, Development.BACKEND, LocalDateTime.now().minusDays(2)))
            );

            logger.info("--- Demo-data has been created in database! ---");
        }
    }

    private Bug createBug(String title, String description, Priority priority, Development development, LocalDateTime bugDate) {
        Bug bug = new Bug();
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setPriority(priority);
        bug.setDevelopment(development);
        bug.setBugDate(bugDate);
        return bug;
    }
}
