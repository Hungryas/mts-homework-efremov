package org.example.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum Error {
    ILLEGAL_ANIMAL_AGE("Возраст не может быть отрицательным"),
    ILLEGAL_ANIMAL_LIST("Массив животных не должен быть пустым");

    private final String message;
}
