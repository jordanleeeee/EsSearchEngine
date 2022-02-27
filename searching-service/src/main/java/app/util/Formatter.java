package app.util;

import org.json.JSONObject;

/**
 * @author Jordan
 */
public class Formatter {
    public static String parseJson(String json) {
        try {
            return new JSONObject(json).toString(2);
        } catch (Exception e) {
            System.out.println(json);
            throw e;
        }
    }
}
