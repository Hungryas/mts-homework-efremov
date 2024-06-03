package org.example.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Animal;
import org.example.repositories.AnimalRepository;
import org.example.services.AnimalSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class AnimalSearchServiceImpl implements AnimalSearchService {

    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    @Autowired
    private AnimalRepository animalRepository;

    @Override
    public Map<String, LocalDate> findLeapYearNames() {
        return animalRepository.findLeapYearNames().stream()
                .filter(animal -> animal.getBirthDate().isLeapYear())
                .collect(Collectors.toMap(
                        animal -> String.format("%s %s", animal.getType().getName(), animal.getName()),
                        Animal::getBirthDate));
    }

    @Override
    public Map<Animal, Integer> findOlderAnimal(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
        Map<Animal, Integer> olderAnimals = animalRepository.findByBirthDateBefore(age).stream()
                .filter(animal -> CURRENT_YEAR - animal.getBirthDate().getYear() > age)
                .collect(Collectors.toMap(
                        Function.identity(),
                        animal -> CURRENT_YEAR - animal.getBirthDate().getYear()));

        if (olderAnimals.isEmpty()) {
            animalRepository.findFirstByOrderByBirthDateAsc()
                    .ifPresent(animal -> olderAnimals.put(animal, CURRENT_YEAR - animal.getBirthDate().getYear()));
        }
        writeToFindOlderAnimalsJson(olderAnimals);

        return olderAnimals;
    }

    private void writeToFindOlderAnimalsJson(Map<Animal, Integer> olderAnimals) {
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
    public Map<String, List<Animal>> findDuplicate() {
        return animalRepository.findAll().stream()
                .collect(Collectors.groupingBy(animal -> animal.getType().getName()));
    }

    @Override
    public double findAverageAge() {
        return animalRepository.findAverageAge();
    }

    @Override
    public List<Animal> findOldAndExpensive() {
        return animalRepository.findOldAndExpensive();
    }

    @Override
    public List<String> findMinCostAnimals() {
        return animalRepository.findMinCostAnimals().stream()
                .map(Animal::getName)
                .sorted(Comparator.reverseOrder())
                .toList();
    }
}
