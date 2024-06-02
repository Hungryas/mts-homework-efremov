package org.starter.services.files;

import org.starter.animals.AbstractAnimal;

import java.io.FileNotFoundException;
import java.util.List;

public interface ResultReader {

    String readSecretInformation() throws FileNotFoundException;

    List<AbstractAnimal> readOlderAnimals() throws FileNotFoundException;
}
