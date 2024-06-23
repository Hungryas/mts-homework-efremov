package org.example.configs;

import org.example.interceptors.CustomInterceptor;
import org.example.services.CreateAnimalService;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DemoApplicationConfig implements WebMvcConfigurer {

    @Bean
    @Scope("prototype")
    CreateAnimalService createAnimalService() {
        return new CreateAnimalServiceImpl();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor());
    }
}
