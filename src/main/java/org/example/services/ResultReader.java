package org.example.services;

import org.example.entities.Animal;

import java.io.FileNotFoundException;
import java.util.List;

public interface ResultReader {

    String readSecretInformation() throws FileNotFoundException;

    List<Animal> readOlderAnimals() throws FileNotFoundException;
}
