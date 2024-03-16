package org.example.errors;

import java.time.LocalDate;

public class InvalidAnimalException extends NullPointerException {

    public InvalidAnimalException() {
        super("На вход пришёл некорректный объект животного " + LocalDate.now());
    }
}
