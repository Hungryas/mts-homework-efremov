package org.example.services.impl;

import org.example.animals.AbstractAnimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateAnimalServiceImplTest {

    private final CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();

    @Test
    @DisplayName("Позитивный тест createAnimals")
    void successCreateAnimals() {
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimals();
        ArrayList<AbstractAnimal> animalList = collectAllAnimalsToList(animals);
        assertThat(animalList).hasSize(10);
        checkLogData(animalList);
    }

    private ArrayList<AbstractAnimal> collectAllAnimalsToList(Map<String, List<AbstractAnimal>> animals) {
        ArrayList<AbstractAnimal> animalCollection = new ArrayList<>();

        for (List<AbstractAnimal> animalList : animals.values()) {
            animalCollection.addAll(animalList);
        }

        return animalCollection;
    }

    private void checkLogData(ArrayList<AbstractAnimal> animalList) {
        List<String> animalLogData = LogDataHelper.readLogData();
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
    @DisplayName("Позитивный тест createAnimals родителя")
    void successCreateAnimalsFromDefault() {
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimalsFromDefault();
        ArrayList<AbstractAnimal> animalList = collectAllAnimalsToList(animals);
        assertThat(animalList).hasSize(10);
        checkLogData(animalList);
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с указанием количества")
    void successCreateAnimalsWithNumber() {
        int number = ThreadLocalRandom.current().nextInt(1, 10);
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimals(number);
        ArrayList<AbstractAnimal> animalList = collectAllAnimalsToList(animals);
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