package org.example.animal.repository;

import lombok.extern.log4j.Log4j2;
import org.example.animal.AbstractAnimal;
import org.example.animal.Animal;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class AnimalRepositoryImpl implements AnimalRepository {

    @Override
    public Map<String, LocalDate> findLeapYearNames(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException("Массив животных не должен быть пустым");
        }
        Map<String, LocalDate> leapYearNames = new HashMap<>();

        for (AbstractAnimal animal : animals) {
            if (animal.getBirthDate().isLeapYear()) {
                String key = animal.getClass().getSimpleName() + animal.getName();
                leapYearNames.put(key, animal.getBirthDate());
            }
        }

        return leapYearNames;
    }

    @Override
    public Map<AbstractAnimal, Integer> findOlderAnimal(List<AbstractAnimal> animals, int age) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException("Массив животных не должен быть пустым");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Возраст должен быть больше нуля");
        }
        Map<AbstractAnimal, Integer> olderAnimals = new HashMap<>();
        int currentYear = LocalDate.now().getYear();
        Map.Entry<AbstractAnimal, Integer> olderAnimal = null;

        for (AbstractAnimal animal : animals) {
            int animalBirthYear = animal.getBirthDate().getYear();
            int animalAge = currentYear - animalBirthYear;

            if (animalAge > age) {
                olderAnimals.put(animal, animalAge);
            } else if (olderAnimals.isEmpty() && (olderAnimal == null || olderAnimal.getValue() < animalAge)) {
                olderAnimal = Map.entry(animal, animalAge);
            }
        }
        if (olderAnimals.isEmpty() && olderAnimal != null) {
            olderAnimals.put(olderAnimal.getKey(), olderAnimal.getValue());
        }

        return olderAnimals;
    }

    @Override
    public Map<String, Integer> findDuplicate(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException("Массив животных не должен быть пустым");
        }
        Map<String, Integer> duplicates = new HashMap<>();

        for (AbstractAnimal animal : animals) {
            String animalType = animal.getClass().getSimpleName();

            if (duplicates.containsKey(animalType)) {
                int count = duplicates.get(animalType);
                duplicates.replace(animalType, count + 1);
            } else {
                duplicates.put(animalType, 1);
            }
        }
        log.info(duplicates);

        return duplicates;
    }
}
