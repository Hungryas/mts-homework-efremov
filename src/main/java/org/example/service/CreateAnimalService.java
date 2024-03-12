package org.example.service;

import org.example.animal.AbstractAnimal;

import java.util.ArrayList;

import static org.example.utils.AnimalHelper.getRandomAnimal;

public interface CreateAnimalService {

    default void createAnimals() {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();
        int i = 0;

        while (i++ < 10) {
            animals.add(getRandomAnimal());
        }
        
        System.out.println("Created in 'while' cycle: " + animals);
    }
}
