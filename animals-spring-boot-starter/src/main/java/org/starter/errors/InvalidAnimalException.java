package org.starter.errors;

import java.time.LocalDate;

public class InvalidAnimalException extends RuntimeException {

    public InvalidAnimalException() {
        super("На вход пришёл некорректный объект животного " + LocalDate.now());
    }
}
