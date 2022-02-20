package util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jordan
 */
public class StringUtils {
    public static List<String> getTags(String content) {
        return Arrays.stream(
                content.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+")
        ).distinct().filter(tag -> tag.length() > 4).toList();
    }

    public static List<String> getTags(List<String> contents) {
        return contents.stream().flatMap(content -> getTags(content).stream()).distinct().toList();
    }
}
