package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.animal.pet.Cat;
import org.example.animal.pet.Dog;
import org.example.animal.predator.Shark;
import org.example.animal.predator.Wolf;
import org.example.service.CreateAnimalService;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Override
    public void createAnimals() {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();
        int i = 0;

        do {
            int index = ThreadLocalRandom.current().nextInt(0, 4);
            AbstractAnimal animal = getAnimalByIndex(index);
            animals.add(animal);
        } while (++i < 10);

        System.out.println("Created in 'do-while' cycle: " + animals);
    }

    public void createAnimals(int number) {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            int index = ThreadLocalRandom.current().nextInt(0, 4);
            AbstractAnimal animal = getAnimalByIndex(index);
            animals.add(animal);
        }

        System.out.println("Created in 'for-i' cycle: " + animals);
    }

    private AbstractAnimal getAnimalByIndex(int index) {
        return switch (index) {
            case 0 -> new Cat();
            case 1 -> new Dog();
            case 2 -> new Shark();
            case 3 -> new Wolf();
            default -> throw new IllegalStateException("Unexpected index: " + index);
        };
    }
}
