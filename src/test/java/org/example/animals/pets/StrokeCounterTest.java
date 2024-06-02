package org.example.animals.pets;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.starter.animals.pets.Cat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@SpringBootTest
class StrokeCounterTest {

    private final Cat cat = new Cat();

    @Test
    @DisplayName("Позитивный тест strokeSum")
    void successStrokeSum() {
        int guests = RandomGenerator.getDefault().nextInt(1, 5);
        log.debug("guests: {}", guests);
        int strokesByOne = RandomGenerator.getDefault().nextInt(10, 100);
        log.debug("strokesByOne: {}", strokesByOne);

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            Runnable strokeAction = () -> IntStream.range(0, strokesByOne)
                    .forEach(i -> cat.stroke());

            for (int i = 0; i < guests; i++) {
                executorService.execute(strokeAction);
            }
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int expectedStrokes = guests * strokesByOne;
        log.debug("expectedStrokes: {}", expectedStrokes);
        assertThat(cat.strokeSum()).isEqualTo(expectedStrokes);
    }
}