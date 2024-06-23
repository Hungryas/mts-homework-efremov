package org.example.services.impl;

import lombok.extern.log4j.Log4j2;
import org.example.services.LogData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@Component
public class LogDataImpl implements LogData {

    @Override
    public void clear() {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
                log.info("logData.txt успешно создан");
            } else {
                Files.writeString(PATH, "");
                log.info("logData.txt успешно очищен");
            }
        } catch (IOException e) {
            log.error("Произошла ошибка при создании/очистке logData.txt: {}", e.getMessage());
        }
    }

    @Override
    public void append(String string) {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
            }
            Files.writeString(PATH, string, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Произошла ошибка при записи в logData.txt: {}", e.getMessage());
        }
    }

    @Override
    public List<String> read() {
        List<String> logData = null;

        try {
            logData = Files.readAllLines(PATH);
        } catch (IOException e) {
            log.error("Произошла ошибка при чтении logData.txt: {}", e.getMessage());
        }
        return logData;
    }

    @Override
    public long getLineCount() {
        long lineCount = 0;

        try (Stream<String> lines = Files.lines(PATH)) {
            lineCount = lines.count();
            log.info("logData.txt содержит {} строк", lineCount);
        } catch (IOException e) {
            log.error("Произошла ошибка при подсчете строк logData.txt: {}", e.getMessage());
        }
        return lineCount;
    }
}
