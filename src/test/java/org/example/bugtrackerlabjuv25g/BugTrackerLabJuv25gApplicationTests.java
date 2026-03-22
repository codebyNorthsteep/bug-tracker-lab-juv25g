package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Test class for the BugTrackerLabJuv25gApplication.
 * <p>
 * This class is responsible for verifying the application context loads properly and
 * includes dependencies for containerized PostgreSQL testing. It uses the Spring Boot
 * testing framework, along with the Testcontainers library to manage a PostgreSQL
 * database container for integration testing.
 * <p>
 * Annotations:
 * - {@code @SpringBootTest}: Indicates that the context of the full application is loaded
 * for testing purposes.
 * - {@code @Testcontainers}: Enables support for Testcontainers, which allows for testing
 * with containerized services.
 * - {@code @Container}: Declares that a Testcontainers-managed container should be started
 * and stopped automatically within the test lifecycle.
 * - {@code @ServiceConnection}: Configures the container to provide a service connection.
 * <p>
 * Fields:
 * - {@code postgresContainer}: A {@link PostgreSQLContainer} configured to use a PostgreSQL
 * image with version 15.3. This container is used to simulate a PostgreSQL database for
 * integration tests.
 * <p>
 * Methods:
 * - {@code contextLoads()}: A basic test method to verify that the Spring application
 * context loads successfully. Utilizes the {@code @Test} annotation for execution.
 */
@SpringBootTest
@Testcontainers
class BugTrackerLabJuv25gApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:latest");

    @Test
    void contextLoads() {
    }

}
