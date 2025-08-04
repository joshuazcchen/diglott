package Configuration;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for loading and accessing Diglott configuration values from a JSON file.
 */
public final class ConfigDataRetriever {

    private static final Path CONFIG_PATH =
            Paths.get(System.getProperty("user.home"), ".diglott", "config.json");

    private static final JSONObject config;

    static {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());

                try (InputStream is = ConfigDataRetriever.class.getClassLoader()
                        .getResourceAsStream("Configuration/config.json")) {
                    if (is == null) {
                        throw new RuntimeException("Default config file not found in JAR resources");
                    }
                    Files.copy(is, CONFIG_PATH);
                }
            }

            String content = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            config = new JSONObject(content);

            String[] requiredKeys = {
                    "input_language", "target_language", "dark_mode", "font_size", "speed", "increment",
                    "original_script", "page_length", "logs", "font", "api_key", "credentials_path"
            };

            Object[] defaultValues = {
                    "en", "fr", "false", 24, 2, true,
                    true, 100, "none", "times new roman", "none", "/your/full/path/to/credentials.json"
            };

            for (int i = 0; i < requiredKeys.length; i++) {
                if (!config.has(requiredKeys[i])) {
                    config.put(requiredKeys[i], defaultValues[i]);
                }
            }

            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or create config: " + e.getMessage(), e);
        }
    }

    private ConfigDataRetriever() {
        // Prevent instantiation
    }

    /**
     * Retrieves a string value by key.
     *
     * @param key the config key
     * @return the associated string value
     */
    public static String get(final String key) {
        return config.getString(key);
    }

    /**
     * Retrieves an int value by key.
     *
     * @param key the config key
     * @return the associated int value
     */
    public static int getInt(final String key) {
        return config.getInt(key);
    }

    /**
     * Retrieves a boolean value by key.
     *
     * @param key the config key
     * @return true if value exists and is true, false otherwise
     */
    public static boolean getBool(final String key) {
        try {
            return config.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets a config key to the specified value.
     *
     * @param key   the key to update
     * @param value the new value
     */
    public static void set(final String key, final Object value) {
        config.put(key, value);
    }

    /**
     * Retrieves the configured translation speed.
     *
     * @return the speed
     */
    public static int getSpeed() {
        return config.getInt("speed");
    }

    /**
     * Saves the config file to disk.
     */
    public static void saveConfig() {
        try (BufferedWriter writer =
                     Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            writer.write(config.toString(4));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the configured font size.
     *
     * @return font size
     */
    public static int getFontSize() {
        return config.getInt("font_size");
    }

    /**
     * Sets and saves the configured font size.
     *
     * @param size the new font size
     */
    public static void setFontSize(final int size) {
        config.put("font_size", size);
        saveConfig();
    }
}
