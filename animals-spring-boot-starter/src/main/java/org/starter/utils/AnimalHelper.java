package org.starter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.starter.animals.AbstractAnimal;
import org.starter.animals.pets.Cat;
import org.starter.animals.pets.Dog;
import org.starter.animals.predators.Shark;
import org.starter.animals.predators.Wolf;

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
