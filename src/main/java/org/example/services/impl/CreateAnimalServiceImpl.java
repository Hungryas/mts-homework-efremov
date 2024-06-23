package org.example.services.impl;

import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.example.entities.Animal;
import org.example.entities.AnimalType;
import org.example.repositories.AnimalTypeRepository;
import org.example.services.CreateAnimalService;
import org.example.services.LogDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Log4j2
@Service
@Primary
public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Autowired
    private LogDataService logDataService;

    @Value("#{'${animal.names}'.split(',')}")
    private List<String> names;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    private Map<String, List<Animal>> animals;

    @Override
    @Scheduled(cron = "${interval-in-cron}")
    public Map<String, List<Animal>> createAnimals() throws FileNotFoundException {
        final int number = 10;
        logDataService.clear();
        animals = new HashMap<>();
        int i = 0;

        do {
            logDataService.append(String.valueOf(++i));
            addRandomAnimal();
        } while (i < number);
        setPredefinedNames();
        log.info("Created {} in 'do-while' cycle: ", number);
        logAnimals();

        return animals;
    }

    @Override
    public Map<String, List<Animal>> createAnimals(int number) throws FileNotFoundException {
        if (number < 1) {
            throw new IllegalArgumentException("Количество животных должно быть положительным");
        }
        logDataService.clear();
        animals = new HashMap<>();

        for (int i = 0; i < number; i++) {
            logDataService.append(String.valueOf(i + 1));
            addRandomAnimal();
        }
        setPredefinedNames();
        log.info("Created {} in 'for-i' cycle: ", number);
        logAnimals();

        return animals;
    }

    private void addRandomAnimal() throws FileNotFoundException {
        Animal animal = createAnimal();
        String animalType = animal.getClass().getSimpleName();

        String animalData = Stream.of(animalType, animal.getName(), animal.getCost(), animal.getBirthDate())
                .map(String::valueOf)
                .collect(Collectors.joining(" ", " ", "\n"));
        logDataService.append(animalData);

        if (!animals.containsKey(animalType)) {
            animals.put(animalType, new ArrayList<>(List.of(animal)));
        } else {
            animals.get(animalType).add(animal);
        }
    }

    private Animal createAnimal() throws FileNotFoundException {
        Animal animal = new Animal();
        AnimalType animalType = animalTypeRepository.getRandomType();
        animal.setType(animalType);
        Faker faker = new Faker();
        animal.setBreed(faker.lorem().word());
        animal.setName(faker.name().firstName());
        animal.setCost(Integer.parseInt(randomNumeric(5)));
        animal.setCharacter(faker.lorem().word());
        animal.setBirthDate(faker.date().birthday(0, 30).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        animal.setSecretInformation(new ResultReaderServiceImpl().readSecretInformation());

        return animal;
    }

    private void setPredefinedNames() {
        Iterator<Animal> animalIterator = animals.values().stream()
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
