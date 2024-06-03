package org.example.services;

import org.example.entities.Animal;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface CreateAnimalService {

    Map<String, List<Animal>> createAnimals() throws FileNotFoundException;

    Map<String, List<Animal>> createAnimals(int number) throws FileNotFoundException;
}
