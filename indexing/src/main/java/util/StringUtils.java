package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jordan
 */
public class StringUtils {
    public static List<String> getTags(String content) {
        List<String> tags = new ArrayList<>();
        List<String> words = Arrays.stream(
                content.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+")
        ).distinct().filter(tag -> tag.length() > 4).toList();

        System.out.println(words);
        for (int i = 1; i <= words.size(); i++) {
            for (int j = 0; j <= words.size() - i; j++) {
                tags.add(String.join(" ", words.subList(j, j + i)));
            }
        }

        return tags;
    }

    public static List<String> getTags(List<String> contents) {
        return contents.stream().flatMap(content -> getTags(content).stream()).distinct().toList();
    }
}
