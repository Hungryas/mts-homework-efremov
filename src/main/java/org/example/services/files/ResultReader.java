package org.example.services.files;

import org.example.animals.AbstractAnimal;

import java.io.FileNotFoundException;
import java.util.List;

public interface ResultReader {

    String readSecretInformation() throws FileNotFoundException;

    List<AbstractAnimal> readOlderAnimals() throws FileNotFoundException;
}
