package org.example;

import org.example.services.CreateAnimalService;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestConfig {

    @Bean(name = "testCreateAnimalService")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        try (var sqlContainer = new PostgreSQLContainer<>("postgres:15-alpine")) {
            return sqlContainer.withInitScript("db_test_init.sql");
        } catch (Exception e) {
            throw new RuntimeException("SQL container wasn't started", e);
        }
    }
}
