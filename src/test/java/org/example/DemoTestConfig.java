package org.example;

import org.example.services.CreateAnimalService;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DemoTestConfig {

    @Bean(name = "testCreateAnimalService")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }
}
