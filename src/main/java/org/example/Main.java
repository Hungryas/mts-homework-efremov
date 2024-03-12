package org.example;

import org.example.service.CreateAnimalService;
import org.example.service.impl.CreateAnimalServiceImpl;
import org.example.service.impl.ZooService;

public class Main {

    public static void main(String[] args) {
        CreateAnimalService zooService = new ZooService();
        zooService.createAnimals();

        CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();
        createAnimalService.createAnimals();
        createAnimalService.createAnimals(10);
    }
}