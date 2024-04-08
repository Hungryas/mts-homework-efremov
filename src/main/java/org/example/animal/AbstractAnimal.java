package org.example.animal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.utils.ResultReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
@Builder
@AllArgsConstructor
@JsonDeserialize(using = AbstractAnimal.Deserializer.class)
@JsonSerialize(using = AbstractAnimal.Serializer.class)
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

    public static class Serializer extends StdSerializer<AbstractAnimal> {

        public Serializer() {
            this(null);
        }

        protected Serializer(Class<AbstractAnimal> vc) {
            super(vc);
        }

        @Override
        public void serialize(AbstractAnimal animal, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField("breed", animal.getBreed());
            jgen.writeStringField("name", animal.getName());
            jgen.writeNumberField("cost", animal.getCost());
            jgen.writeStringField("character", animal.getCharacter());
            jgen.writeStringField("birthDate", animal.getBirthDate().toString());
            String encodedSecretInformation = Base64.getEncoder().encodeToString(animal.getSecretInformation().getBytes());
            jgen.writeStringField("secretInformation", encodedSecretInformation);
            jgen.writeEndObject();
        }
    }

    public static class Deserializer extends StdDeserializer<AbstractAnimal> {

        public Deserializer() {
            this(null);
        }

        protected Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public AbstractAnimal deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);

            JsonNode breedNode = node.get("breed");
            JsonNode nameNode = node.get("name");
            JsonNode costNode = node.get("cost");
            JsonNode characterNode = node.get("character");
            JsonNode birthDateNode = node.get("birthDate");
            JsonNode secretInformationNode = node.get("secretInformation");
            byte[] decodedSecretInformationBytes = Base64.getDecoder().decode(secretInformationNode.asText());

            return AbstractAnimal.builder()
                    .breed(breedNode.asText())
                    .name(nameNode.asText())
                    .cost(costNode.asDouble())
                    .character(characterNode.asText())
                    .birthDate(LocalDate.parse(birthDateNode.asText()))
                    .secretInformation(new String(decodedSecretInformationBytes))
                    .build();
        }
    }
}
