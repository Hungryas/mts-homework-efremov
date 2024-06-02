package org.example;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.starter.services.CreateAnimalService;
import org.starter.services.impl.CreateAnimalServiceImpl;

@TestConfiguration
public class TestConfig {

    @Bean(name = "testCreateAnimalService")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }
}
