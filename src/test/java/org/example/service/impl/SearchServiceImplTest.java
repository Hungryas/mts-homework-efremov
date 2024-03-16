package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.errors.InvalidAnimalException;
import org.example.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.utils.AnimalHelper.getRandomAnimal;

class SearchServiceImplTest {

    private final SearchService searchService = new SearchServiceImpl();

    private final AbstractAnimal animal = getRandomAnimal();

    @ParameterizedTest(name = "{displayName} [{index}] year=''{0}''")
    @DisplayName("Позитивный тест checkLeapYearAnimal")
    @ValueSource(ints = {2000, 2001})
    void successCheckLeapYearAnimal(int year) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AbstractAnimal testAnimal = animal.toBuilder()
                .birthDate(LocalDate.of(year, 1, 1))
                .build();

        searchService.checkLeapYearAnimal(testAnimal);
        String actualOutContent = outContent.toString().trim();

        if (testAnimal.getBirthDate().isLeapYear()) {
            assertThat(actualOutContent).isEqualTo(testAnimal.getName() + " был рожден в високосный год");
        } else {
            assertThat(actualOutContent).isEqualTo(testAnimal.getName() + " не был рожден в високосный год");
        }
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalException")
    void failureCheckLeapYearAnimalWithInvalidAnimalException() {
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(null))
                .isInstanceOf(InvalidAnimalException.class)
                .hasMessageStartingWith("На вход пришёл некорректный объект животного");
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalBirthDateException")
    void failureCheckLeapYearAnimalWithInvalidAnimalBirthDateException() {
        AbstractAnimal testAnimal = animal.toBuilder()
                .birthDate(null)
                .build();
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(testAnimal))
                .isInstanceOf(InvalidAnimalBirthDateException.class)
                .hasMessageStartingWith("У животного %s не указана дата его рождения".formatted(testAnimal.getClass().getSimpleName()));
    }
}