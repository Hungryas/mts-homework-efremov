package org.example.animals.repositories;

import org.example.animals.AbstractAnimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnimalRepository {

    Map<String, LocalDate> findLeapYearNames(List<AbstractAnimal> animals);

    Map<AbstractAnimal, Integer> findOlderAnimal(List<AbstractAnimal> animals, int age);

    Map<String, List<AbstractAnimal>> findDuplicate(List<AbstractAnimal> animals);

    void findAverageAge(List<AbstractAnimal> animals);

    List<AbstractAnimal> findOldAndExpensive(List<AbstractAnimal> animals);

    List<String> findMinCostAnimals(List<AbstractAnimal> animals);
}
