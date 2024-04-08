package org.example.services.impl;

import lombok.extern.log4j.Log4j2;
import org.example.animals.AbstractAnimal;
import org.example.services.CreateAnimalService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.utils.AnimalHelper.getRandomAnimal;
import static org.example.utils.LogDataHelper.appendLogData;
import static org.example.utils.LogDataHelper.clearLogData;

@Log4j2
public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Override
    public Map<String, List<AbstractAnimal>> createAnimals() {
        clearLogData();
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();
        int i = 0;

        log.info("Created in 'do-while' cycle: ");
        do {
            appendLogData(String.valueOf(++i));
            addRandomAnimals(animals);
        } while (++i < 10);

        return animals;
    }

    public Map<String, List<AbstractAnimal>> createAnimalsFromDefault() {
        return CreateAnimalService.super.createAnimals();
    }

    public Map<String, List<AbstractAnimal>> createAnimals(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Количество животных должно быть положительным");
        }
        clearLogData();
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
        appendLogData(animalData);

        if (!animals.containsKey(animalType)) {
            animals.put(animalType, new ArrayList<>(List.of(animal)));
        } else {
            animals.get(animalType).add(animal);
        }
    }
}
