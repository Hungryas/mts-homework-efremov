package org.example.animals.pets;

import com.github.javafaker.Faker;
import lombok.ToString;
import org.example.animals.AbstractAnimal;

@ToString(callSuper = true)
public abstract class Pet extends AbstractAnimal implements StrokeCounter {

    protected String owner;

    protected Pet() {
        super();
        Faker faker = new Faker();
        this.owner = faker.name().fullName();
    }
}
