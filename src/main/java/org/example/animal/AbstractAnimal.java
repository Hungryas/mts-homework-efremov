package org.example.animal;

import lombok.Getter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
public abstract class AbstractAnimal implements Animal {

    protected String breed;

    protected String name;

    protected Double cost;

    protected String character;

    protected AbstractAnimal() {
        this.breed = randomAlphabetic(4, 12);
        this.name = randomAlphabetic(4, 12);
        this.cost = Double.valueOf(randomNumeric(5));
        this.character = randomAlphabetic(4, 12);
    }

    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }

    @Override
    public String toString() {
        return "breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", character='" + character + '\'';
    }
}
