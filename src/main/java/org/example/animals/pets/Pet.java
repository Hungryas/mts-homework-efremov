package org.example.animals.pets;

import lombok.ToString;
import net.datafaker.Faker;
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
