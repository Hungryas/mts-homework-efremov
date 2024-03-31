package org.example.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum Error {
    ILLEGAL_NEGATIVE("%s не может быть отрицательным"),
    ILLEGAL_EMPTY("%s не должен быть пустым");

    private final String message;

    public String messageWith(String parameter) {
        return message.formatted(parameter);
    }
}
