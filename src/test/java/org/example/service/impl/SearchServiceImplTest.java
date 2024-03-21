package org.example.service.impl;

import org.example.animal.AbstractAnimal;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.errors.InvalidAnimalException;
import org.example.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.utils.AnimalHelper.getRandomAnimal;

class SearchServiceImplTest {

    private final SearchService searchService = new SearchServiceImpl();

    private final AbstractAnimal animal = getRandomAnimal();

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с невисокосным годом")
    void successCheckLeapYearAnimalIsFalse() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AbstractAnimal testAnimal = animal.toBuilder()
                .birthDate(LocalDate.of(2001, 1, 1))
                .build();

        searchService.checkLeapYearAnimal(testAnimal);
        String actualOutContent = outContent.toString().trim();

        assertThat(actualOutContent).isEqualTo(testAnimal.getName() + " не был рожден в високосный год");
    }

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с високосным годом")
    void successCheckLeapYearAnimalIsTrue() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AbstractAnimal testAnimal = animal.toBuilder()
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        searchService.checkLeapYearAnimal(testAnimal);
        String actualOutContent = outContent.toString().trim();

        assertThat(actualOutContent).isEqualTo(testAnimal.getName() + " был рожден в високосный год");
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalException")
    void failureCheckLeapYearAnimalWithInvalidAnimalException() {
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(null))
                .isInstanceOf(InvalidAnimalException.class)
                .hasMessageMatching("На вход пришёл некорректный объект животного \\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalBirthDateException")
    void failureCheckLeapYearAnimalWithInvalidAnimalBirthDateException() {
        AbstractAnimal testAnimal = animal.toBuilder()
                .birthDate(null)
                .build();
        String expectedMessage = "У животного %s не указана дата его рождения".formatted(testAnimal.getClass().getSimpleName());
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(testAnimal))
                .isInstanceOf(InvalidAnimalBirthDateException.class)
                .hasMessageContaining(expectedMessage);
    }
}