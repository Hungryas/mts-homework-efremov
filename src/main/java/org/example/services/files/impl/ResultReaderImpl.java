package org.example.services.files.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.example.animals.AbstractAnimal;
import org.example.services.files.ResultReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
public class ResultReaderImpl implements ResultReader {

    @Override
    public String readSecretInformation() throws FileNotFoundException {
        Path path = Paths.get("src", "main", "resources", "secretStore", "secretInformation.txt");

        if (!Files.exists(path)) {
            throw new FileNotFoundException("secretInformation.txt существует");
        }
        String secretInformation = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            secretInformation = reader.readLine();
        } catch (IOException e) {
            log.error("Произошла ошибка при чтении secretInformation.txt: {}", e.getMessage());
        }
        return secretInformation;
    }

    @Override
    public List<AbstractAnimal> readOlderAnimals() throws FileNotFoundException {
        File file = Paths.get("src", "main", "resources", "results", "findOlderAnimals.json").toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("secretInformation.txt существует");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<AbstractAnimal> animals = null;

        try {
            animals = mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Произошла ошибка при чтении findOlderAnimals.json: {}", e.getMessage());

        }
        log.info("Из findOlderAnimals.json получен список: {}", animals);

        return animals;
    }
}
