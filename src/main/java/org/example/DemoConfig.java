package org.example;

import org.example.services.impl.CreateAnimalServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
public class DemoConfig {

    @Bean(name = "createAnimalService")
    @Scope("prototype")
    CreateAnimalServiceImpl createAnimalServiceImpl() {
        return new CreateAnimalServiceImpl();
    }
}
