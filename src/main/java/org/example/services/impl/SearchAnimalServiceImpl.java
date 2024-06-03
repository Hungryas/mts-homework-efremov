package org.example.services.impl;

import org.example.entities.Animal;
import org.example.errors.InvalidAnimalException;
import org.example.errors.RequiredPropertyMissedException;
import org.example.services.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchAnimalServiceImpl implements SearchService {

    @Override
    public boolean checkLeapYearAnimal(Animal animal) {
        if (animal == null) {
            throw new InvalidAnimalException();
        }
        if (animal.getBirthDate() == null) {
            throw new RequiredPropertyMissedException("birthDate");
        }
        return animal.getBirthDate().isLeapYear();
    }
}
