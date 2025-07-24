package Configuration;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ConfigDataRetriever {

    private static final Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".diglott", "config.json");

    private static JSONObject config;

    static {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                // Create config directory if needed
                Files.createDirectories(CONFIG_PATH.getParent());

                // Read default config from resources
                InputStream is = ConfigDataRetriever.class.getClassLoader()
                        .getResourceAsStream("Configuration/config.json");
                if (is == null) {
                    throw new RuntimeException("Default config file not found in JAR resources");
                }

                // Write default config to user directory
                Files.copy(is, CONFIG_PATH);
            }

            // Load user config
            String content = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            config = new JSONObject(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or create config: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return config.getString(key);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static boolean getBool(String key) {
        try {
            return config.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static void set(String key, Object value) {
        config.put(key, value);
    }

    public static int getSpeed() {
        return config.getInt("speed");
    }

    public static void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            writer.write(config.toString(4));
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
