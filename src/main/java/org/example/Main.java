package org.example;

import org.example.animal.pet.Cat;
import org.example.animal.pet.Pet;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.service.impl.CreateAnimalServiceImpl;
import org.example.service.impl.SearchServiceImpl;

public class Main {

    public static void main(String[] args) {
        CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();
        createAnimalService.createAnimalsFromDefault();
        createAnimalService.createAnimals();
        createAnimalService.createAnimals(10);

        SearchServiceImpl searchService = new SearchServiceImpl();
        Pet pet = new Cat();

        try {
            searchService.checkLeapYearAnimal(pet);
        } catch (InvalidAnimalBirthDateException e) {
            throw new InvalidAnimalBirthDateException(e.getCause());
        }
    }
}