package org.example.services;

import org.example.TestConfig;
import org.example.entities.Animal;
import org.example.services.impl.LogDataImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestConfig.class)
class CreateAnimalServiceTest {

    @Autowired
    @Qualifier("testCreateAnimalService")
    private CreateAnimalService createAnimalService;

    @Test
    @DisplayName("Позитивный тест createAnimals")
    void successCreateAnimals() throws FileNotFoundException {
        Map<String, List<Animal>> animals = createAnimalService.createAnimals();
        ArrayList<Animal> animalList = collectAllAnimalsToList(animals);
        assertThat(animalList).hasSize(10);
        checkLogData(animalList);
    }

    private ArrayList<Animal> collectAllAnimalsToList(Map<String, List<Animal>> animals) {
        ArrayList<Animal> animalCollection = new ArrayList<>();

        for (List<Animal> animalList : animals.values()) {
            animalCollection.addAll(animalList);
        }

        return animalCollection;
    }

    private void checkLogData(ArrayList<Animal> animalList) {
        List<String> animalLogData = new LogDataImpl().read();
        assertThat(animalList).hasSameSizeAs(animalLogData);

        for (String animalLog : animalLogData) {
            String[] animalProperties = animalLog.split(" ");
            boolean isMatchedAnimal = animalList.stream()
                    .filter(animal -> animal.getClass().getSimpleName().equals(animalProperties[0]))
                    .allMatch(animal -> Objects.equals(animal.getName(), animalProperties[2]) &&
                            Objects.equals(animal.getCost().toString(), animalProperties[3]) &&
                            Objects.equals(animal.getBirthDate().toString(), animalProperties[4]));
            assertThat(isMatchedAnimal).isTrue();
        }
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с указанием количества")
    void successCreateAnimalsWithNumber() throws FileNotFoundException {
        int number = ThreadLocalRandom.current().nextInt(1, 10);
        Map<String, List<Animal>> animals = createAnimalService.createAnimals(number);
        ArrayList<Animal> animalList = collectAllAnimalsToList(animals);
        assertThat(animalList).hasSize(number);
        checkLogData(animalList);
    }

    @Test
    @DisplayName("Негативный тест createAnimals с указанием количества")
    void failureCreateAnimalsWithNumber() {
        int number = ThreadLocalRandom.current().nextInt(-1, 1);
        assertThatThrownBy(() -> createAnimalService.createAnimals(number))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Количество животных должно быть положительным");
    }
}