package org.example.errors;

public class RequiredPropertyMissedException extends RuntimeException {

    public RequiredPropertyMissedException(String propertyName) {
        super("Не указано обязательное поле %s!".formatted(propertyName));
    }
}
