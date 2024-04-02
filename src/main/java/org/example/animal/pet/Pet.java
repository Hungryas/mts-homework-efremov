package org.example.animal.pet;

import com.github.javafaker.Faker;
import org.example.animal.AbstractAnimal;

public abstract class Pet extends AbstractAnimal implements StrokeCounter {

    protected String owner;

    protected Pet() {
        super();
        Faker faker = new Faker();
        this.owner = faker.name().fullName();
    }

    @Override
    public String toString() {
        return
                super.toString() +
                        ", owner='" + owner + '\'' +
                        '}';
    }
}
