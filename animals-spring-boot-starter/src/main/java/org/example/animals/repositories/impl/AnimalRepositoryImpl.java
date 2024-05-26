package org.example.animals.repositories.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.example.animals.AbstractAnimal;
import org.example.animals.repositories.AnimalRepository;
import org.example.services.CreateAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class AnimalRepositoryImpl implements AnimalRepository {

    private static final String ILLEGAL_EMPTY_ERROR_MESSAGE = "массив животных не должен быть пустым";

    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    @Autowired
    private CreateAnimalService createAnimalService;

    @PostConstruct
    private void createAnimals() {
        createAnimalService.createAnimals();
    }

    @Override
    public Map<String, LocalDate> findLeapYearNames(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }
        return animals.stream()
                .filter(animal -> animal.getBirthDate().isLeapYear())
                .collect(Collectors.toMap(
                        animal -> String.format("%s %s", animal.getClass().getSimpleName(), animal.getName()),
                        AbstractAnimal::getBirthDate));
    }

    @Override
    public Map<AbstractAnimal, Integer> findOlderAnimal(List<AbstractAnimal> animals, int age) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }
        if (age < 0) {
            throw new IllegalArgumentException("возраст не может быть отрицательным");
        }
        Map<AbstractAnimal, Integer> olderAnimals = animals.stream()
                .filter(animal -> CURRENT_YEAR - animal.getBirthDate().getYear() > age)
                .collect(Collectors.toMap(
                        Function.identity(),
                        animal -> CURRENT_YEAR - animal.getBirthDate().getYear()));

        if (olderAnimals.isEmpty()) {
            animals.stream()
                    .min(Comparator.comparing(animal -> animal.getBirthDate().getYear()))
                    .ifPresent(animal -> olderAnimals.put(animal, CURRENT_YEAR - animal.getBirthDate().getYear()));
        }
        writeToFindOlderAnimalsJson(olderAnimals);

        return olderAnimals;
    }

    private void writeToFindOlderAnimalsJson(Map<AbstractAnimal, Integer> olderAnimals) {

        try {
            Path path = Paths.get("src", "main", "resources", "results", "findOlderAnimals.json");
            if (!Files.exists(path)) {
                Files.createFile(path);
                log.info("findOlderAnimal.json успешно создан");
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.registerModule(new JavaTimeModule());
            String jsonString = mapper.writeValueAsString(olderAnimals.keySet());
            Files.writeString(path, jsonString);
        } catch (IOException e) {
            log.error("Произошла ошибка при записи findOlderAnimal.json: {}", e.getMessage());
        }
    }

    @Override
    public Map<String, List<AbstractAnimal>> findDuplicate(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }

        return animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getClass().getSimpleName()));
    }

    @Override
    public void findAverageAge(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }
        animals.stream()
                .mapToInt(animal -> CURRENT_YEAR - animal.getBirthDate().getYear())
                .average()
                .ifPresent(age -> System.out.println("Средний возраст равен " + age));
    }

    @Override
    public List<AbstractAnimal> findOldAndExpensive(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }
        double averageCost = animals.stream()
                .mapToDouble(AbstractAnimal::getCost)
                .average().orElseThrow();

        return animals.stream()
                .filter(animal -> CURRENT_YEAR - animal.getBirthDate().getYear() > 5)
                .filter(animal -> animal.getCost() > averageCost)
                .sorted(Comparator.comparing(AbstractAnimal::getBirthDate))
                .toList();
    }

    @Override
    public List<String> findMinCostAnimals(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_EMPTY_ERROR_MESSAGE);
        }
        return animals.stream()
                .sorted(Comparator.comparing(AbstractAnimal::getCost))
                .limit(3)
                .map(AbstractAnimal::getName)
                .sorted(Comparator.reverseOrder())
                .toList();
    }
}
