package org.example.animal;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.utils.ResultReader;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
@Builder
@AllArgsConstructor
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
        this.secretInformation = ResultReader.readSecretInformation();
    }

    @JsonGetter("secretInformation")
    private String getEncodedSecretInformation() {
        return Base64.getEncoder().encodeToString(this.secretInformation.getBytes());
    }

    @JsonSetter("secretInformation")
    private void setDecodedSecretInformation(String secretInformation) {
        byte[] decodeBytes = Base64.getDecoder().decode(secretInformation);
        this.secretInformation = new String(decodeBytes);
    }

    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{'" +
                "breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", character='" + character + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", secretInformation='" + secretInformation + '\'';
    }
}
