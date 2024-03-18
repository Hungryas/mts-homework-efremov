package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.service.CreateAnimalService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.utils.AnimalHelper.getRandomAnimal;

public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Override
    public Map<String, List<AbstractAnimal>> createAnimals() {
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();
        int i = 0;

        do {
            addRandomAnimals(animals);
        } while (++i < 10);

        System.out.println("Created in 'do-while' cycle: " + animals);

        return animals;
    }

    public Map<String, List<AbstractAnimal>> createAnimalsFromDefault() {
        return CreateAnimalService.super.createAnimals();
    }

    public Map<String, List<AbstractAnimal>> createAnimals(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Количество животных должно быть положительным");
        }
        Map<String, List<AbstractAnimal>> animals = new HashMap<>();

        for (int i = 0; i < number; i++) {
            addRandomAnimals(animals);
        }

        System.out.println("Created in 'for-i' cycle: " + animals);

        return animals;
    }

    private void addRandomAnimals(Map<String, List<AbstractAnimal>> animals) {
        AbstractAnimal animal = getRandomAnimal();
        String animalType = animal.getClass().getSimpleName();

        if (!animals.containsKey(animalType)) {
            animals.put(animalType, new ArrayList<>(List.of(animal)));
        } else {
            animals.get(animalType).add(animal);
        }
    }
}
