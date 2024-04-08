package org.example.utils;

import org.example.animal.AbstractAnimal;
import org.example.animal.pet.Cat;
import org.example.animal.repository.AnimalRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ResultReaderTest {

    @Test
    @DisplayName("Позитивный тест readSecretInformation")
    void successReadSecretInformation() throws FileNotFoundException {
        String secretInformation = ResultReader.readSecretInformation();
        assertThat(secretInformation).isNotEmpty();
    }

    @Test
    @DisplayName("Позитивный тест readOlderAnimals")
    void successReadSOlderAnimals() throws FileNotFoundException {
        Cat cat = new Cat();
        cat.setBirthDate(LocalDate.of(2000, 1, 1));
        new AnimalRepositoryImpl().findOlderAnimal(List.of(cat), 1);
        List<AbstractAnimal> abstractAnimals = ResultReader.readOlderAnimals();
        assertThat(abstractAnimals).hasSize(1);

        boolean isMatched = abstractAnimals.stream()
                .allMatch(animal -> Objects.equals(animal.getName(), cat.getName()) &&
                        Objects.equals(animal.getBreed(), cat.getBreed()) &&
                        Objects.equals(animal.getCost(), cat.getCost()) &&
                        Objects.equals(animal.getCharacter(), cat.getCharacter()) &&
                        Objects.equals(animal.getBirthDate(), cat.getBirthDate()) &&
                        Objects.equals(animal.getSecretInformation(), cat.getSecretInformation()));
        assertThat(isMatched).isTrue();
    }
}