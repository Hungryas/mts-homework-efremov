package org.example.repositories;

import org.example.TestConfig;
import org.example.entities.Animal;
import org.example.entities.AnimalType;
import org.example.services.AnimalSearchService;
import org.example.services.impl.ResultReaderImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestConfig.class)
class AnimalSearchServiceTest {

    private static final AnimalType catType = new AnimalType();

    private static final AnimalType dogType = new AnimalType();

    private static final AnimalType wolfType = new AnimalType();

    private static final Animal cat1 = new Animal();

    private static final Animal cat2 = new Animal();

    private static final Animal dog1 = new Animal();

    private static final Animal dog2 = new Animal();

    private static final Animal wolf = new Animal();

    @Autowired
    private AnimalSearchService animalSearchService;

    @BeforeAll
    static void setup() {
        catType.setName("cat");
        catType.setIsPredator(true);

        dogType.setName("dog");
        dogType.setIsPredator(true);

        wolfType.setName("wolf");
        wolfType.setIsPredator(true);

        cat1.setName("Alysha");
        cat1.setType(catType);
        cat1.setCost(100);
        cat1.setBirthDate(LocalDate.of(2000, 1, 1));

        cat2.setName("Alfredo");
        cat2.setType(catType);
        cat2.setCost(100);
        cat2.setBirthDate(LocalDate.of(2001, 1, 1));

        dog1.setName("Annette");
        dog1.setType(dogType);
        dog1.setCost(200);
        dog1.setBirthDate(LocalDate.of(2002, 1, 1));

        dog2.setName("Abel");
        dog2.setType(dogType);
        dog2.setCost(1500);
        dog2.setBirthDate(LocalDate.of(2003, 1, 1));

        wolf.setName("Aaron");
        wolf.setType(wolfType);
        wolf.setCost(1000);
        wolf.setBirthDate(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("Позитивный тест findLeapYearNames")
    void successFindLeapYearNames() {
        Map<String, LocalDate> leapYearNames = animalSearchService.findLeapYearNames();
        assertThat(leapYearNames).containsExactlyInAnyOrderEntriesOf(Map.of("cat Alysha", cat1.getBirthDate(),
                                                                            "wolf Aaron", wolf.getBirthDate()));
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом меньше возраста младшего из животных")
    void successFindOlderAnimals() throws FileNotFoundException {
        int age = LocalDate.now().getYear() - wolf.getBirthDate().getYear();
        Map<Animal, Integer> olderAnimals = animalSearchService.findOlderAnimal(age);
        assertThat(olderAnimals).hasSize(4)
                .doesNotContainKey(wolf);
        checkFindOlderAnimalsJson(olderAnimals);
    }

    private void checkFindOlderAnimalsJson(Map<Animal, Integer> olderAnimals) throws FileNotFoundException {
        List<Animal> animalsFromFile = new ResultReaderImpl().readOlderAnimals();
        assertThat(olderAnimals).hasSameSizeAs(animalsFromFile);

        for (Animal animalFromFile : animalsFromFile) {
            boolean isMatchedAnimal = olderAnimals.keySet().stream()
                    .filter(animal -> animal.getName().equals(animalFromFile.getName()))
                    .allMatch(animal -> Objects.equals(animal.getBreed(), animalFromFile.getBreed()) &&
                            Objects.equals(animal.getCost(), animalFromFile.getCost()) &&
                            Objects.equals(animal.getCharacter(), animalFromFile.getCharacter()) &&
                            Objects.equals(animal.getBirthDate(), animalFromFile.getBirthDate()) &&
                            Objects.equals(animal.getSecretInformation(), animalFromFile.getSecretInformation()));
            assertThat(isMatchedAnimal).isTrue();
        }
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом больше возраста любого животного")
    void successFindOlderAnimalOfPossible() throws FileNotFoundException {
        Map<Animal, Integer> olderAnimals = animalSearchService.findOlderAnimal(Integer.MAX_VALUE);
        int expectedAge = LocalDate.now().getYear() - cat1.getBirthDate().getYear();
        assertThat(olderAnimals).containsValues(expectedAge);
        checkFindOlderAnimalsJson(olderAnimals);
    }

    @Test
    @DisplayName("Негативный тест findOlderAnimal возраста животных")
    void failureFindOlderAnimalWithBadAge() {
        assertThatThrownBy(() -> animalSearchService.findOlderAnimal(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Возраст не может быть отрицательным");
    }

    @Test
    @DisplayName("Позитивный тест findDuplicate")
    void successFindDuplicate() {
        Map<String, List<Animal>> duplicates = animalSearchService.findDuplicate();
        assertThat(duplicates.get("cat")).hasSize(2);
        assertThat(duplicates.get("dog")).hasSize(2);
        assertThat(duplicates.get("wolf")).hasSize(1);
    }

    @Test
    @DisplayName("Позитивный тест findAverageAge")
    void successFindAverageAge() {
        double averageAge = animalSearchService.findAverageAge();
        assertThat(averageAge).isEqualTo(18.0);
    }

    @Test
    @DisplayName("Позитивный тест findOldAndExpensive")
    void successFindOldAndExpensive() {
        List<Animal> oldAndExpensiveAnimals = animalSearchService.findOldAndExpensive();
        assertThat(oldAndExpensiveAnimals).hasSize(1);
        assertThat(oldAndExpensiveAnimals.getFirst().getCost()).isEqualTo(dog2.getCost());
    }

    @Test
    @DisplayName("Позитивный тест findMinCostAnimals")
    void successFindMinCostAnimals() {
        List<String> minCostAnimals = animalSearchService.findMinCostAnimals();
        assertThat(minCostAnimals).containsExactly(dog1.getName(), cat1.getName(), cat2.getName());
    }
}