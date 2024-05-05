package org.example.services.impl;

import org.example.animals.AbstractAnimal;
import org.example.errors.InvalidAnimalBirthDateException;
import org.example.errors.InvalidAnimalException;
import org.example.services.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.utils.AnimalHelper.getRandomAnimal;

@SpringBootTest
class SearchServiceImplTest {

    @Autowired
    private SearchService searchService;

    private final AbstractAnimal animal = getRandomAnimal();

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с невисокосным годом")
    void successCheckLeapYearAnimalIsFalse() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        animal.setBirthDate(LocalDate.of(2001, 1, 1));
        searchService.checkLeapYearAnimal(animal);
        String actualOutContent = outContent.toString().trim();

        assertThat(actualOutContent).isEqualTo(animal.getName() + " не был рожден в високосный год");
    }

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с високосным годом")
    void successCheckLeapYearAnimalIsTrue() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        animal.setBirthDate(LocalDate.of(2000, 1, 1));
        searchService.checkLeapYearAnimal(animal);
        String actualOutContent = outContent.toString().trim();

        assertThat(actualOutContent).isEqualTo(animal.getName() + " был рожден в високосный год");
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalException")
    void failureCheckLeapYearAnimalWithInvalidAnimalException() {
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(null))
                .isInstanceOf(InvalidAnimalException.class)
                .hasMessage("На вход пришёл некорректный объект животного %s".formatted(LocalDate.now()));
    }

    @Test
    @DisplayName("Негативный тест checkLeapYearAnimal с InvalidAnimalBirthDateException")
    void failureCheckLeapYearAnimalWithInvalidAnimalBirthDateException() {
        animal.setBirthDate(null);
        String expectedMessage = "У животного %s не указана дата его рождения".formatted(animal.getClass().getSimpleName());
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(animal))
                .isInstanceOf(InvalidAnimalBirthDateException.class)
                .hasMessageContaining(expectedMessage);
    }
}