package org.example;

import org.example.services.CreateAnimalService;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DemoConfig {

    @Bean
    @Scope("prototype")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }
}
