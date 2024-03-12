package org.example.animal;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

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

    public String getBreed() {
        return this.breed;
    }

    public String getName() {
        return this.name;
    }

    public Double getCost() {
        return this.cost;
    }

    public String getCharacter() {
        return this.character;
    }

    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }

    @Override
    public String toString() {
        return "\n" + this.getClass().getSimpleName() + "{'" +
                "breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", character='" + character + '\'';
    }
}
