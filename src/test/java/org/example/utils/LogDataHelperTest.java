package org.example.utils;

import org.example.service.impl.CreateAnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogDataHelperTest {

    public static final int ENTRIES_COUNT = 10;

    private final CreateAnimalServiceImpl createAnimalService = new CreateAnimalServiceImpl();

    @BeforeEach
    void createLogData() {
        createAnimalService.createAnimals(ENTRIES_COUNT);
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с очисткой файла")
    void successClearLogDataWithClearing() {
        LogDataHelper.clearLogData();
        long lineCount = LogDataHelper.getLineCount();
        assertThat(lineCount).isZero();
    }

    @Test
    @DisplayName("Позитивный тест createAnimals с удалением файла")
    void successClearLogDataWithDeleting() throws IOException {
        Files.delete(LogDataHelper.PATH);
        LogDataHelper.clearLogData();
        long lineCount = LogDataHelper.getLineCount();
        assertThat(lineCount).isZero();
    }

    @Test
    @DisplayName("Позитивный тест appendLogData")
    void successAppendLogData() {
        LogDataHelper.appendLogData("appended_text");
        long lineCount = LogDataHelper.getLineCount();
        assertThat(lineCount).isEqualTo(ENTRIES_COUNT + 1);
    }

    @Test
    @DisplayName("Позитивный тест appendLogData записи в пустой файл")
    void successAppendLogDataWithEmptyFile() throws IOException {
        Files.delete(LogDataHelper.PATH);
        LogDataHelper.appendLogData("appended_text");
        long lineCount = LogDataHelper.getLineCount();
        assertThat(lineCount).isOne();
    }

    @Test
    @DisplayName("Позитивный тест createAnimals")
    void readLogData() {
        List<String> logDataList = LogDataHelper.readLogData();
        Long lineCount = LogDataHelper.getLineCount();
        assertThat(logDataList).hasSize(lineCount.intValue());
    }

    @Test
    @DisplayName("Позитивный тест getLineCount")
    void successGetLineCount() {
        long lineCount = LogDataHelper.getLineCount();
        assertThat(lineCount).isEqualTo(ENTRIES_COUNT);
    }
}