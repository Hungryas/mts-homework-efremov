package org.example.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface LogDataService {

    Path PATH = Paths.get("src", "main", "resources", "animals", "logData.txt");

    void clear();

    void append(String string);

    List<String> read();

    long getLineCount();
}
