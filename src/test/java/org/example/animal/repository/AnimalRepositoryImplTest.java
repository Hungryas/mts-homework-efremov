package org.example.animal.repository;

import org.example.animal.AbstractAnimal;
import org.example.animal.pet.Cat;
import org.example.animal.pet.Dog;
import org.example.animal.predator.Shark;
import org.example.animal.predator.Wolf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnimalRepositoryImplTest {

    public static final String ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE = "Массив животных не должен быть пустым";

    private static final List<AbstractAnimal> animals = new ArrayList<>();

    private static final Cat cat1 = new Cat();

    private static final Cat cat2 = new Cat();

    private static final Dog dog = new Dog();

    private static final Shark shark = new Shark();

    private static final Wolf wolf = new Wolf();

    private final AnimalRepository animalRepository = new AnimalRepositoryImpl();

    @BeforeAll
    static void setup() {
        cat1.setName("Alysha");
        cat1.setCost(100.0);
        cat1.setBirthDate(LocalDate.of(2000, 1, 1));

        cat2.setName("Alfredo");
        cat2.setCost(100.0);
        cat2.setBirthDate(LocalDate.of(2001, 1, 1));

        dog.setName("Annette");
        dog.setCost(200.0);
        dog.setBirthDate(LocalDate.of(2002, 1, 1));

        shark.setName("Abel");
        shark.setCost(1500.0);
        shark.setBirthDate(LocalDate.of(2003, 1, 1));

        wolf.setName("Aaron");
        wolf.setCost(1000.0);
        wolf.setBirthDate(LocalDate.of(2024, 1, 1));

        animals.addAll(List.of(cat1, cat2, dog, shark, wolf));
    }

    @Test
    @DisplayName("Позитивный тест findLeapYearNames")
    void successFindLeapYearNames() {
        Map<String, LocalDate> leapYearNames = animalRepository.findLeapYearNames(animals);
        assertThat(leapYearNames).containsExactlyInAnyOrderEntriesOf(Map.of("Cat Alysha", cat1.getBirthDate(),
                                                                            "Wolf Aaron", wolf.getBirthDate()));
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findLeapYearNames")
    @NullAndEmptySource
    void failureFindLeapYearNames(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findLeapYearNames(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом меньше возраста одного из животных")
    void successFindOlderAnimal() {
        int age = LocalDate.now().getYear() - wolf.getBirthDate().getYear();
        Map<AbstractAnimal, Integer> olderAnimals = animalRepository.findOlderAnimal(animals, age);
        assertThat(olderAnimals).hasSize(4)
                .doesNotContainKey(wolf);
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом больше возраста любого животного")
    void successFindOlderAnimalOfPossible() {
        Map<AbstractAnimal, Integer> olderAnimals = animalRepository.findOlderAnimal(animals, Integer.MAX_VALUE);
        int expectedAge = LocalDate.now().getYear() - cat1.getBirthDate().getYear();
        assertThat(olderAnimals).containsExactlyEntriesOf(Map.of(cat1, expectedAge));
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findOlderAnimal массива животных")
    @NullAndEmptySource
    void failureFindOlderAnimalWithBadList(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findOlderAnimal(animals, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Негативный тест findOlderAnimal возраста животных")
    void failureFindOlderAnimalWithBadAge() {
        assertThatThrownBy(() -> animalRepository.findOlderAnimal(animals, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Возраст не может быть отрицательным");
    }

    @Test
    @DisplayName("Позитивный тест findDuplicate")
    void successFindDuplicate() {
        Map<String, List<AbstractAnimal>> duplicates = animalRepository.findDuplicate(animals);
        assertThat(duplicates).containsExactlyInAnyOrderEntriesOf(Map.of("Cat", List.of(cat1, cat2), "Dog", List.of(dog),
                                                                         "Shark", List.of(shark), "Wolf", List.of(wolf)));
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findDuplicate")
    @NullAndEmptySource
    void failureFindDuplicate(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findDuplicate(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Позитивный тест findAverageAge")
    void successFindAverageAge() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        animalRepository.findAverageAge(animals);
        String actualOutContent = outContent.toString().trim();
        assertThat(actualOutContent).isEqualTo("Средний возраст равен 18.0");
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findAverageAge")
    @NullAndEmptySource
    void failureFindAverageAge(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findAverageAge(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Позитивный тест findOldAndExpensive")
    void successFindOldAndExpensive() {
        List<AbstractAnimal> oldAndExpensiveAnimals = animalRepository.findOldAndExpensive(animals);
        assertThat(oldAndExpensiveAnimals).containsExactly(shark);
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findOldAndExpensive")
    @NullAndEmptySource
    void failureFindOldAndExpensive(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findOldAndExpensive(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Позитивный тест findMinCostAnimals")
    void successFindMinCostAnimals() {
        List<String> minCostAnimals = animalRepository.findMinCostAnimals(animals);
        assertThat(minCostAnimals).containsExactly(dog.getName(), cat1.getName(), cat2.getName());
    }

    @ParameterizedTest(name = "{displayName} [{index}] animals={0}")
    @DisplayName("Негативный тест findMinCostAnimals")
    @NullAndEmptySource
    void failureFindMinCostAnimals(List<AbstractAnimal> animals) {
        assertThatThrownBy(() -> animalRepository.findMinCostAnimals(animals))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_ANIMAL_LIST_ERROR_MESSAGE);
    }
}