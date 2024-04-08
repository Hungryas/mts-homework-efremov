package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogDataHelper {

    public static final Path PATH = Paths.get("src", "main", "resources", "animals", "logData.txt");

    public static void clearLogData() {
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

    public static void appendLogData(String string) {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
            }
            Files.writeString(PATH, string, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Произошла ошибка при записи в logData.txt: {}", e.getMessage());
        }
    }

    public static List<String> readLogData() {
        List<String> logData = null;

        try {
            logData = Files.readAllLines(PATH);
        } catch (IOException e) {
            log.error("Произошла ошибка при чтении logData.txt: {}", e.getMessage());
        }
        return logData;
    }

    public static long getLineCount() {
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
