package org.example.service;

import org.example.animal.AbstractAnimal;
import org.example.animal.pet.Cat;
import org.example.animal.pet.Dog;
import org.example.animal.predator.Shark;
import org.example.animal.predator.Wolf;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public interface CreateAnimalService {

    default void createAnimals() {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();
        int i = 0;

        while (i++ < 10) {
            int index = ThreadLocalRandom.current().nextInt(0, 4);
            AbstractAnimal animal = getAnimalByIndex(index);
            animals.add(animal);
        }

        System.out.println("Created in 'while' cycle: " + animals);
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
