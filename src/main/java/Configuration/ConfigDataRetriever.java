package Configuration;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigDataRetriever {

    private static final String CONFIG_PATH = "src/main/java/Configuration/config.json";

    private static JSONObject config;

    static {
        String content;
        try {
            InputStream is = ConfigDataRetriever.class.getClassLoader().getResourceAsStream("Configuration/config.json");
            if (is == null) {
                throw new RuntimeException("Config file not found in resources");
            }
            byte[] bytes = is.readAllBytes();
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = new JSONObject(content);
    }

    //TODO: switch this to an Anything type
    public static String get(String key) {
        return config.getString(key);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }
    public static boolean getBool(String key) {
        return config.getBoolean(key);
    }
    public static void set(String key, String value) {
        config.put(key, value);
    }

    public static int getSpeed() {
        return config.getInt("speed");
    }

    public static void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CONFIG_PATH), StandardCharsets.UTF_8)) {
            writer.write(config.toString(4));  // pretty print with 4 spaces indent
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file: " + e.getMessage(), e);
        }
    }

    public static int getFontSize() {
        return config.getInt("font_size");
    }

    public static void setFontSize(int size) {
        config.put("font_size", size);
        saveConfig();
    }

}
