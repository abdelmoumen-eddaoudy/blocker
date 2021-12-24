package org.ensaf.blocker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Handlers {
    public static @NotNull List<Path> listFiles(Path path) throws IOException {
        List<Path> temp_list;
        Vector<Path> result = new Vector<>();
        try (Stream<Path> walk = Files.walk(path)) {
            temp_list = walk.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path path1 : temp_list) {
                if (path1.toFile().toString().endsWith(".pdf")) {
                    result.add(path1);
                }
            }
        }
        return result;
    }
}
