package org.example.services.impl;

import lombok.extern.log4j.Log4j2;
import org.example.animals.AbstractAnimal;
import org.example.services.CreateAnimalService;
import org.example.services.files.LogData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.utils.AnimalHelper.getRandomAnimal;

@Log4j2
@Service
@PropertySource("/application.properties")
public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Autowired
    private LogData logData;

    @Value("#{'${animal.cat.names}'.split(',')}")
    private List<String> names;

    private Map<String, List<AbstractAnimal>> animals;

    @Override
    @Scheduled(cron = "${interval-in-cron}")
    public Map<String, List<AbstractAnimal>> createAnimals() {
        logData.clear();
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();
        int i = 0;

        log.info("Created in 'do-while' cycle: ");
        do {
            logData.append(String.valueOf(++i));
            addRandomAnimals(animals);
        } while (i < 10);

        return animals;
    }

    public Map<String, List<AbstractAnimal>> createAnimalsFromDefault() {
        return CreateAnimalService.super.createAnimals();
    }

    public Map<String, List<AbstractAnimal>> createAnimals(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Количество животных должно быть положительным");
        }
        logData.clear();
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();

        log.info("Created in 'for-i' cycle: ");
        for (int i = 0; i < number; i++) {
            addRandomAnimals(animals);
        }

        return animals;
    }

    private void addRandomAnimals(Map<String, List<AbstractAnimal>> animals) {
        AbstractAnimal animal = getRandomAnimal();
        log.info(animal);
        String animalType = animal.getClass().getSimpleName();

        String animalData = Stream.of(animalType, animal.getName(), animal.getCost(), animal.getBirthDate())
                .map(String::valueOf)
                .collect(Collectors.joining(" ", " ", "\n"));
        logData.append(animalData);

        if (!animals.containsKey(animalType)) {
            animals.put(animalType, new ArrayList<>(List.of(animal)));
        } else {
            animals.get(animalType).add(animal);
        }
    }
}
