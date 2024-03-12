package org.example.animal.pet;

import org.example.animal.AbstractAnimal;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public abstract class Pet extends AbstractAnimal {

    protected String owner;

    protected Pet() {
        super();
        this.owner = randomAlphabetic(4, 12);
    }

    @Override
    public String toString() {
        return
                super.toString() +
                        ", owner='" + owner + '\'' +
                        '}';
    }
}
