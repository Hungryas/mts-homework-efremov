package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateAnimalServiceImplTest {

    private final CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();

    @Test
    @DisplayName("Позитивный тест createAnimals")
    void successCreateAnimals() {
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimals();
        ArrayList<AbstractAnimal> objects = collectAllAnimalsToList(animals);
        assertThat(objects).hasSize(10);
    }

    private ArrayList<AbstractAnimal> collectAllAnimalsToList(Map<String, List<AbstractAnimal>> animals) {
        ArrayList<AbstractAnimal> animalCollection = new ArrayList<>();

        for (List<AbstractAnimal> animalList : animals.values()) {
            animalCollection.addAll(animalList);
        }
        
        return animalCollection;
    }

    @Test
    @DisplayName("Позитивный тест createAnimals родителя")
    void successCAnimalsFromDefault() {
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimalsFromDefault();
        ArrayList<AbstractAnimal> objects = collectAllAnimalsToList(animals);
        assertThat(objects).hasSize(10);
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с указанием количества")
    void successCreateAnimalsWithNumber() {
        int number = ThreadLocalRandom.current().nextInt(1, 10);
        Map<String, List<AbstractAnimal>> animals = createAnimalService.createAnimals(number);
        ArrayList<AbstractAnimal> objects = collectAllAnimalsToList(animals);
        assertThat(objects).hasSize(number);
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