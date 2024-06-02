package org.starter.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starter.animals.AbstractAnimal;
import org.starter.services.CreateAnimalService;
import org.starter.services.files.LogData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.starter.utils.AnimalHelper.getRandomAnimal;

@Log4j2
@Service
public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Autowired
    private LogData logData;

    @Value("#{'${animal.names}'.split(',')}")
    private List<String> names;

    private Map<String, List<AbstractAnimal>> animals;

    @Override
    @Scheduled(cron = "${interval-in-cron}")
    public Map<String, List<AbstractAnimal>> createAnimals() {
        final int number = 10;
        logData.clear();
        animals = new HashMap<>();
        int i = 0;

        do {
            logData.append(String.valueOf(++i));
            addRandomAnimal();
        } while (i < number);
        setPredefinedNames();
        log.info("Created {} in 'do-while' cycle: ", number);
        logAnimals();

        return animals;
    }

    @Override
    public Map<String, List<AbstractAnimal>> createAnimals(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Количество животных должно быть положительным");
        }
        logData.clear();
        animals = new HashMap<>();

        for (int i = 0; i < number; i++) {
            logData.append(String.valueOf(i + 1));
            addRandomAnimal();
        }
        setPredefinedNames();
        log.info("Created {} in 'for-i' cycle: ", number);
        logAnimals();

        return animals;
    }

    private void addRandomAnimal() {
        AbstractAnimal animal = getRandomAnimal();
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

    private void setPredefinedNames() {
        Iterator<AbstractAnimal> animalIterator = animals.values().stream()
                .flatMap(List::stream)
                .toList().iterator();
        Collections.shuffle(names);
        Iterator<String> defineNamesIterator = new ArrayList<>(names).iterator();

        while (animalIterator.hasNext() && defineNamesIterator.hasNext()) {
            String name = defineNamesIterator.next();
            animalIterator.next().setName(name);
        }
    }

    private void logAnimals() {
        animals.values().stream()
                .flatMap(List::stream)
                .forEach(log::info);
    }
}
