package org.example.animal.repository;

import org.example.animal.AbstractAnimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnimalRepository {

    Map<String, LocalDate> findLeapYearNames(List<AbstractAnimal> animals);

    Map<AbstractAnimal, Integer> findOlderAnimal(List<AbstractAnimal> animals, int age);

    Map<String, Integer> findDuplicate(List<AbstractAnimal> animals);
}
