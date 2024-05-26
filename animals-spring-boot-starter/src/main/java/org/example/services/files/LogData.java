package org.example.services.files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface LogData {

    Path PATH = Paths.get("src", "main", "resources", "animals", "logData.txt");

    void clear();

    void append(String string);

    List<String> read();

    long getLineCount();
}
