package org.example.animal.predator;

import com.github.javafaker.Faker;
import org.example.animal.AbstractAnimal;

public abstract class Predator extends AbstractAnimal {

    protected String habitat;

    protected Predator() {
        super();
        Faker faker = new Faker();
        this.habitat = faker.lorem().word();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", habitat='" + habitat + '\'' +
                '}';
    }
}
