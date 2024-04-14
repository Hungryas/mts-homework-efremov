package org.example.services;

import org.example.animals.AbstractAnimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.utils.AnimalHelper.getRandomAnimal;

public interface CreateAnimalService {

    default Map<String, List<AbstractAnimal>> createAnimals() {
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();
        int i = 0;
        System.out.println("Created in 'while' cycle:");

        while (i++ < 10) {
            AbstractAnimal animal = getRandomAnimal();
            String animalType = animal.getClass().getSimpleName();

            if (!animals.containsKey(animalType)) {
                animals.put(animalType, new ArrayList<>(List.of(animal)));
            } else {
                animals.get(animalType).add(animal);
            }
            System.out.println(animal);
        }

        return animals;
    }
}
