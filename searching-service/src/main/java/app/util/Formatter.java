package app.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jordan
 */
public class Formatter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Formatter.class);
    public static String parseJson(String json) {
        try {
            return new JSONObject(json).toString(2);
        } catch (Exception e) {
            LOGGER.error("parse json exception ", e);
            return "";
        }
    }
}
