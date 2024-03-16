package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.errors.InvalidAnimalException;
import org.example.service.SearchService;

public class SearchServiceImpl implements SearchService {

    @Override
    public void checkLeapYearAnimal(AbstractAnimal animal) throws InvalidAnimalBirthDateException {
        if (animal == null) {
            throw new InvalidAnimalException();
        }
        if (animal.getBirthDate() == null) {
            throw new InvalidAnimalBirthDateException(animal);
        }

        if (animal.getBirthDate().isLeapYear()) {
            System.out.println(animal.getName() + " был рожден в високосный год");
        } else {
            System.out.println(animal.getName() + " не был рожден в високосный год");
        }
    }
}
