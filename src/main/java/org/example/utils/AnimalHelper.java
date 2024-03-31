package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.animal.AbstractAnimal;
import org.example.animal.pet.Cat;
import org.example.animal.pet.Dog;
import org.example.animal.predator.Shark;
import org.example.animal.predator.Wolf;

import java.util.random.RandomGenerator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalHelper {

    public static AbstractAnimal getRandomAnimal() {
        return switch (RandomGenerator.getDefault().nextInt(4)) {
            case 0 -> new Cat();
            case 1 -> new Dog();
            case 2 -> new Shark();
            case 3 -> new Wolf();
            default -> throw new IllegalStateException("Unexpected index");
        };
    }
}
