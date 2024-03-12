package org.example.animal.predator;

import org.example.animal.AbstractAnimal;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public abstract class Predator extends AbstractAnimal {

    protected String habitat;

    protected Predator() {
        super();
        this.habitat = randomAlphabetic(4, 12);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{'" +
                super.toString() +
                ", habitat='" + habitat + '\'' +
                '}';
    }
}
