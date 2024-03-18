package org.example.animal.repository;

import org.example.animal.AbstractAnimal;
import org.example.animal.Animal;
import org.example.animal.pet.Cat;
import org.example.animal.pet.Dog;
import org.example.animal.pet.Pet;
import org.example.animal.predator.Predator;
import org.example.animal.predator.Shark;
import org.example.animal.predator.Wolf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnimalRepositoryImplTest {

    private static final List<AbstractAnimal> animals = new ArrayList<>();

    private final AnimalRepository animalRepository = new AnimalRepositoryImpl();

    @BeforeAll
    static void setup() {
        Pet cat = new Cat();
        cat.setBirthDate(LocalDate.of(2000, 1, 1));
        Pet dog = new Dog();
        dog.setBirthDate(LocalDate.of(2001, 1, 1));
        Predator shark = new Shark();
        shark.setBirthDate(LocalDate.of(2002, 1, 1));
        Predator wolf = new Wolf();
        wolf.setBirthDate(LocalDate.of(2003, 1, 1));

        animals.addAll(List.of(cat, dog, shark, wolf));
    }

    @Test
    @DisplayName("Позитивный тест findLeapYearNames")
    void successFindLeapYearNames() {
        Map<String, LocalDate> leapYearNames = animalRepository.findLeapYearNames(animals);
        assertThat(leapYearNames).hasSize(1)
                .containsValue(LocalDate.of(2000, 1, 1));
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findLeapYearNames")
    @NullAndEmptySource
    void failureFindLeapYearNames(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findLeapYearNames(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("Массив животных не должен быть пустым");
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом меньше возраста одного из животных")
    void successFindOlderAnimal() {
        AbstractAnimal expectedAnimal = animals.getFirst();
        int age = LocalDate.now().getYear() - expectedAnimal.getBirthDate().getYear() - 1;
        Map<Animal, Integer> olderAnimals = animalRepository.findOlderAnimal(animals, age);
        assertThat(olderAnimals).hasSize(1)
                .containsKey(expectedAnimal);
        Map<AbstractAnimal, Integer> olderAnimals = animalRepository.findOlderAnimal(animals, age);
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом больше возраста любого животного")
    void successFindOlderAnimalOfPossible() {
        AbstractAnimal expectedAnimal = animals.getFirst();
        Map<AbstractAnimal, Integer> olderAnimals = animalRepository.findOlderAnimal(animals, Integer.MAX_VALUE);
        assertThat(olderAnimals).hasSize(1)
                .containsKey(expectedAnimal);
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findOlderAnimal")
    @NullAndEmptySource
    void failureFindOlderAnimal(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findOlderAnimal(animals, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("Массив животных не должен быть пустым");
    }

    @Test
    @DisplayName("Позитивный тест findDuplicate")
    void successFindDuplicate() {
        Map<String, Integer> duplicates = animalRepository.findDuplicate(animals);
        assertThat(duplicates).hasSize(4)
                .containsOnlyKeys("Cat", "Dog", "Shark", "Wolf")
                .containsValue(1);
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findDuplicate")
    @NullAndEmptySource
    void failureFindDuplicate(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findDuplicate(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("Массив животных не должен быть пустым");
    }
}