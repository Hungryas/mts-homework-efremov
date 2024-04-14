package org.example;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.example.animals.pets.Cat;
import org.example.animals.pets.Pet;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.example.services.impl.SearchServiceImpl;
import org.example.utils.MathFunctions;

@Log4j2
public class Main {

    public static void main(String[] args) {
        CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();
        createAnimalService.createAnimalsFromDefault();
        createAnimalService.createAnimals();
        createAnimalService.createAnimals(10);

        SearchServiceImpl searchService = new SearchServiceImpl();
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