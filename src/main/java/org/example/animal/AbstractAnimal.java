package org.example.animal;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class AbstractAnimal implements Animal {

    protected String breed;

    protected String name;

    protected Double cost;

    protected String character;

    protected AbstractAnimal() {
        Faker faker = new Faker();
        this.breed = faker.lorem().word();
        this.name = faker.name().firstName();
        this.cost = Double.valueOf(randomNumeric(5));
        this.character = faker.lorem().word();
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
