package org.example.utils;

import org.example.TestConfig;
import org.example.entities.Animal;
import org.example.services.AnimalSearchService;
import org.example.services.ResultReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
class ResultReaderTest {

    @Autowired
    private ResultReader resultReader;

    @Autowired
    private AnimalSearchService animalSearchService;

    @Test
    @DisplayName("Позитивный тест readSecretInformation")
    void successReadSecretInformation() throws FileNotFoundException {
        String secretInformation = resultReader.readSecretInformation();
        assertThat(secretInformation).isNotEmpty();
    }

    @Test
    @DisplayName("Позитивный тест readOlderAnimals")
    void successReadOlderAnimals() throws FileNotFoundException {
        int age = ThreadLocalRandom.current().nextInt(15);
        Set<Animal> expectedAnimals = animalSearchService.findOlderAnimal(age).keySet();
        List<Animal> actualAnimals = resultReader.readOlderAnimals();
        assertThat(actualAnimals).hasSameSizeAs(expectedAnimals);

        for (Animal actualAnimal : actualAnimals) {
            Animal expectedAnimal = expectedAnimals.stream()
                    .filter(animal -> animal.getName().equals(actualAnimal.getName()))
                    .findFirst().orElseThrow();
            assertThat(actualAnimal.getId()).isEqualTo(expectedAnimal.getId());
            assertThat(actualAnimal.getCost()).isEqualTo(expectedAnimal.getCost());
            assertThat(actualAnimal.getName()).isEqualTo(expectedAnimal.getName());
            assertThat(actualAnimal.getBreed()).isEqualTo(expectedAnimal.getBreed());
            assertThat(actualAnimal.getCharacter()).isEqualTo(expectedAnimal.getCharacter());
            assertThat(actualAnimal.getBirthDate()).isEqualTo(expectedAnimal.getBirthDate());
        }
    }
}