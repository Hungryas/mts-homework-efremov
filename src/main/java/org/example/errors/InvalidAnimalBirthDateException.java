package org.example.errors;

import org.example.animal.AbstractAnimal;

public class InvalidAnimalBirthDateException extends RuntimeException {

    public InvalidAnimalBirthDateException(AbstractAnimal animal) {
        super("У животного %s не указана дата его рождения".formatted(animal.getClass().getSimpleName()));
    }

    public InvalidAnimalBirthDateException(Throwable cause) {
        super("Работа метода завершилась с ошибкой", cause);
    }
}
