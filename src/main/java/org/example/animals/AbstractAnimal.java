package org.example.animals;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import net.datafaker.Faker;
import org.example.services.files.impl.ResultReaderImpl;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
@ToString
public class AbstractAnimal implements Animal {

    protected String breed;

    @Setter
    protected String name;

    @Setter
    protected Double cost;

    protected String character;

    @Setter
    protected LocalDate birthDate;

    protected String secretInformation;

    @SneakyThrows
    protected AbstractAnimal() {
        Faker faker = new Faker();
        this.breed = faker.lorem().word();
        this.name = faker.name().firstName();
        this.cost = Double.valueOf(randomNumeric(5));
        this.character = faker.lorem().word();
        this.birthDate = faker.date().birthday(0, 30).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.secretInformation = new ResultReaderImpl().readSecretInformation();
    }

    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }
}
