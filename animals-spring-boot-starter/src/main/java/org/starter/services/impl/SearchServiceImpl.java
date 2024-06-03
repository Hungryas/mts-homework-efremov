package org.starter.services.impl;

import org.springframework.stereotype.Service;
import org.starter.animals.AbstractAnimal;
import org.starter.errors.InvalidAnimalBirthDateException;
import org.starter.errors.InvalidAnimalException;
import org.starter.services.SearchService;

@Service
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
            System.out.println(animal.getName() + " был рожден в невисокосный год");
        }
    }
}
