package org.example.animals.predators;

import com.github.javafaker.Faker;
import lombok.ToString;
import org.example.animals.AbstractAnimal;

@ToString(callSuper = true)
public abstract class Predator extends AbstractAnimal {

    protected String habitat;

    protected Predator() {
        super();
        Faker faker = new Faker();
        this.habitat = faker.lorem().word();
    }
}
