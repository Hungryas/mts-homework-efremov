package org.example.services;

import org.example.TestConfig;
import org.example.entities.Animal;
import org.example.errors.InvalidAnimalException;
import org.example.errors.RequiredPropertyMissedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestConfig.class)
class SearchAnimalServiceTest {

    @Autowired
    private SearchService searchService;

    private final Animal animal = new Animal();

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с невисокосным годом")
    void successCheckLeapYearAnimalIsFalse() {
        animal.setBirthDate(LocalDate.of(2001, 1, 1));
        boolean leapYearAnimal = searchService.checkLeapYearAnimal(animal);
        assertThat(leapYearAnimal).isFalse();
    }

    @Test
    @DisplayName("Позитивный тест checkLeapYearAnimal с високосным годом")
    void successCheckLeapYearAnimalIsTrue() {
        animal.setBirthDate(LocalDate.of(2000, 1, 1));
        boolean leapYearAnimal = searchService.checkLeapYearAnimal(animal);
        assertThat(leapYearAnimal).isTrue();
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
        assertThatThrownBy(() -> searchService.checkLeapYearAnimal(animal))
                .isInstanceOf(RequiredPropertyMissedException.class)
                .hasMessageContaining("Не указано обязательное поле birthDate!");
    }
}