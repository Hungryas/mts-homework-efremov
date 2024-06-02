package org.example;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.example.utils.MathFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.starter.animals.pets.Cat;
import org.starter.animals.pets.Pet;
import org.starter.errors.InvalidAnimalBirthDateException;
import org.starter.services.CreateAnimalService;
import org.starter.services.SearchService;
import org.starter.services.impl.CreateAnimalServiceImpl;

@Log4j2
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class);
        CreateAnimalService createAnimalService = context.getBean("createAnimalService", CreateAnimalServiceImpl.class);
        createAnimalService.createAnimals();
        createAnimalService.createAnimals(10);

        SearchService searchService = context.getBean(SearchService.class);
        Pet pet = new Cat();

        try {
            searchService.checkLeapYearAnimal(pet);
        } catch (InvalidAnimalBirthDateException e) {
            throw new InvalidAnimalBirthDateException(e.getCause());
        }

        int size = 10000000;
        StopWatch singleThreadWatch = StopWatch.createStarted();
        MathFunctions.generateNumbersInSingleThreadMode(size);
        singleThreadWatch.stop();
        log.info("Время генерации {} случайных числе в один поток, мс: {}", size, singleThreadWatch.getTime());

        StopWatch multiThreadedWatch = StopWatch.createStarted();
        MathFunctions.generateNumbersInMultiThreadedMode(size);
        multiThreadedWatch.stop();
        log.info("Время генерации {} случайных числе в несколько потоков, мс: {}", size, multiThreadedWatch.getTime());
    }
}
