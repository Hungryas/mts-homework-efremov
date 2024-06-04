package org.example.services;

import org.example.TestConfig;
import org.example.entities.Animal;
import org.example.entities.AnimalType;
import org.example.repositories.AnimalRepository;
import org.example.repositories.AnimalTypeRepository;
import org.example.services.impl.ResultReaderImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnimalSearchServiceTest {

    private final Animal cat1 = new Animal();

    private final Animal cat2 = new Animal();

    private final Animal dog1 = new Animal();

    private final Animal dog2 = new Animal();

    private final Animal wolf = new Animal();

    @Autowired
    private AnimalSearchService animalSearchService;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @BeforeAll
    void setup() {
        AnimalType catType = new AnimalType();
        catType.setName("cat");
        catType.setIsPredator(true);

        AnimalType dogType = new AnimalType();
        dogType.setName("dog");
        dogType.setIsPredator(true);

        AnimalType wolfType = new AnimalType();
        wolfType.setName("wolf");
        wolfType.setIsPredator(true);

        animalTypeRepository.saveAll(List.of(catType, dogType, wolfType));

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

        animalRepository.saveAll(List.of(cat1, cat2, dog1, dog2, wolf));
    }

    @AfterAll
    void teardown() {
        animalRepository.deleteAll();
        animalTypeRepository.deleteAll();
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
        assertThat(olderAnimals).containsKeys(cat1, cat2, dog1, dog2)
                .doesNotContainKey(wolf);
        checkFindOlderAnimalsJson(olderAnimals);
    }

    private void checkFindOlderAnimalsJson(Map<Animal, Integer> olderAnimals) throws FileNotFoundException {
        List<Animal> animalsFromFile = new ResultReaderImpl().readOlderAnimals();
        assertThat(olderAnimals).containsOnlyKeys(animalsFromFile);
    }

    @Test
    @DisplayName("Позитивный тест findOlderAnimal с возрастом больше возраста любого животного")
    void successFindOlderAnimalOfPossible() throws FileNotFoundException {
        Map<Animal, Integer> olderAnimals = animalSearchService.findOlderAnimal(Integer.MAX_VALUE);
        int expectedAge = LocalDate.now().getYear() - cat1.getBirthDate().getYear();
        assertThat(olderAnimals).containsValues(expectedAge)
                .containsKey(cat1);
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
        assertThat(duplicates.get("cat")).containsOnly(cat1, cat2);
        assertThat(duplicates.get("dog")).containsOnly(dog1, dog2);
        assertThat(duplicates.get("wolf")).containsOnly(wolf);
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
        assertThat(oldAndExpensiveAnimals).containsOnly(dog2);
    }

    @Test
    @DisplayName("Позитивный тест findMinCostAnimals")
    void successFindMinCostAnimals() {
        List<String> minCostAnimals = animalSearchService.findMinCostAnimals();
        assertThat(minCostAnimals).containsExactly(dog1.getName(), cat1.getName(), cat2.getName());
    }
}