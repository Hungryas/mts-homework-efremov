package org.example;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.example.services.CreateAnimalService;
import org.example.utils.MathFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;

@Log4j2
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws FileNotFoundException {
        ApplicationContext context = SpringApplication.run(DemoApplication.class);
        CreateAnimalService createAnimalService = context.getBean(CreateAnimalService.class);
        createAnimalService.createAnimals();
        createAnimalService.createAnimals(10);

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
