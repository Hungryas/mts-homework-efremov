package org.example.utils;

import org.example.TestConfig;
import org.example.services.LogData;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
class LogDataHelperTest {

    private static final int ENTRIES_COUNT = 10;

    @Autowired
    private LogData logData;

    @Autowired
    private CreateAnimalServiceImpl createAnimalService;

    @BeforeEach
    void createLogData() throws FileNotFoundException {
        createAnimalService.createAnimals(ENTRIES_COUNT);
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с очисткой файла")
    void successClearWithClearing() {
        logData.clear();
        long lineCount = logData.getLineCount();
        assertThat(lineCount).isZero();
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с удалением файла")
    void successClearWithDeleting() throws IOException {
        Files.delete(logData.PATH);
        logData.clear();
        long lineCount = logData.getLineCount();
        assertThat(lineCount).isZero();
    }

    @Test
    @DisplayName("Позитивный тест appendLogData")
    void successAppend() {
        logData.append("appended_text");
        long lineCount = logData.getLineCount();
        assertThat(lineCount).isEqualTo(ENTRIES_COUNT + 1);
    }

    @Test
    @DisplayName("Позитивный тест appendLogData записи в пустой файл")
    void successAppendWithEmptyFile() throws IOException {
        Files.delete(logData.PATH);
        logData.append("appended_text");
        long lineCount = logData.getLineCount();
        assertThat(lineCount).isOne();
    }

    @Test
    @DisplayName("Позитивный тест createAnimals")
    void successRead() {
        List<String> logDataList = logData.read();
        Long lineCount = logData.getLineCount();
        assertThat(logDataList).hasSize(lineCount.intValue());
    }

    @Test
    @DisplayName("Позитивный тест getLineCount")
    void successGetLineCount() {
        long lineCount = logData.getLineCount();
        assertThat(lineCount).isEqualTo(ENTRIES_COUNT);
    }
}