package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Jordan
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static void save(String path, String content) {
        if (!new File(path).exists()) {
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write(content);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static Optional<String> read(String path) {
        try {
            return Optional.of(new String(new FileInputStream(path).readAllBytes()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static String readOrElseThrow(String path) {
        try {
            return new String(new FileInputStream(path).readAllBytes());
        } catch (IOException e) {
            throw new Error("file not found, path=" + path);
        }
    }
}
