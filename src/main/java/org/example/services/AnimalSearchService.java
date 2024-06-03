package org.example.services;

import org.example.entities.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnimalSearchService {

    Map<String, LocalDate> findLeapYearNames();

    Map<Animal, Integer> findOlderAnimal(int age);

    Map<String, List<Animal>> findDuplicate();

    double findAverageAge();

    List<Animal> findOldAndExpensive();

    List<String> findMinCostAnimals();
}
