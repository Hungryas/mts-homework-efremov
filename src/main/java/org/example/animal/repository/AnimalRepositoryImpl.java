package org.example.animal.repository;

import lombok.extern.log4j.Log4j2;
import org.example.animal.AbstractAnimal;
import org.example.errors.Error;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class AnimalRepositoryImpl implements AnimalRepository {

    @Override
    public Map<String, LocalDate> findLeapYearNames(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
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
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
        }
        if (age < 0) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_AGE.message());
        }
        int currentYear = LocalDate.now().getYear();
        Map<AbstractAnimal, Integer> olderAnimals = animals.stream()
                .filter(animal -> currentYear - animal.getBirthDate().getYear() > age)
                .collect(Collectors.toMap(
                        Function.identity(),
                        animal -> currentYear - animal.getBirthDate().getYear()));

        if (olderAnimals.isEmpty()) {
            animals.stream()
                    .min(Comparator.comparing(animal -> animal.getBirthDate().getYear()))
                    .ifPresent(animal -> olderAnimals.put(animal, currentYear - animal.getBirthDate().getYear()));
        }

        return olderAnimals;
    }

    @Override
    public Map<String, List<AbstractAnimal>> findDuplicate(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
        }

        return animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getClass().getSimpleName()));
    }

    @Override
    public void findAverageAge(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
        }
        int currentYear = LocalDate.now().getYear();
        animals.stream()
                .mapToInt(animal -> currentYear - animal.getBirthDate().getYear())
                .average()
                .ifPresent(age -> System.out.println("Средний возраст равен " + age));
    }

    @Override
    public List<AbstractAnimal> findOldAndExpensive(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
        }
        int currentYear = LocalDate.now().getYear();
        double averageCost = animals.stream()
                .filter(animal -> currentYear - animal.getBirthDate().getYear() > 5)
                .mapToDouble(AbstractAnimal::getCost)
                .average().orElseThrow();

        return animals.stream()
                .filter(animal -> animal.getCost() > averageCost)
                .sorted(Comparator.comparing(AbstractAnimal::getBirthDate))
                .toList();
    }

    @Override
    public List<String> findMinCostAnimals(List<AbstractAnimal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalArgumentException(Error.ILLEGAL_ANIMAL_LIST.message());
        }
        return animals.stream()
                .sorted(Comparator.comparing(AbstractAnimal::getCost))
                .limit(3)
                .map(AbstractAnimal::getName)
                .sorted(Comparator.reverseOrder())
                .toList();
    }
}
