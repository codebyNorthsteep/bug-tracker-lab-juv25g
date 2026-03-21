package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
class BugTrackerLabJuv25gApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:15.3");

    @Test
    void contextLoads() {
    }

}
