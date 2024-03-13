package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.service.CreateAnimalService;

import java.util.ArrayList;

import static org.example.utils.AnimalHelper.getRandomAnimal;

public class CreateAnimalServiceImpl implements CreateAnimalService {

    @Override
    public void createAnimals() {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();
        int i = 0;

        do {
            animals.add(getRandomAnimal());
        } while (++i < 10);

        System.out.println("Created in 'do-while' cycle: " + animals);
    }

    public void createAnimalsFromDefault() {
        CreateAnimalService.super.createAnimals();
    }

    public void createAnimals(int number) {
        ArrayList<AbstractAnimal> animals = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            animals.add(getRandomAnimal());
        }

        System.out.println("Created in 'for-i' cycle: " + animals);
    }
}
