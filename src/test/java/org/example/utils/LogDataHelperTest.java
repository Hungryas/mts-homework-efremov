package org.example.utils;

import org.example.services.files.LogData;
import org.example.services.files.impl.LogDataImpl;
import org.example.services.impl.CreateAnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogDataHelperTest {

    public static final int ENTRIES_COUNT = 10;

    public final LogData logData = new LogDataImpl();

    private final CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();

    @BeforeEach
    void createLogData() {
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