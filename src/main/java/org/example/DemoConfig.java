package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.starter.services.CreateAnimalService;
import org.starter.services.files.impl.LogDataImpl;
import org.starter.services.impl.CreateAnimalServiceImpl;
import org.starter.services.impl.SearchServiceImpl;

@Configuration
public class DemoConfig {

    @Bean
    @Scope("prototype")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }

    @Bean
    LogDataImpl logData() {
        return new LogDataImpl();
    }

    @Bean
    SearchServiceImpl searchService() {
        return new SearchServiceImpl();
    }
}
