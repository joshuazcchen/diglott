package configuration;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Utility class for loading and accessing Diglott configuration values from a JSON file.
 */
public final class ConfigDataRetriever {

    private static final Path CONFIG_PATH =
            Paths.get(System.getProperty("user.home"), ".diglott", "config.json");

    private static final JSONObject CONFIG;

    static {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());

                try (InputStream is = ConfigDataRetriever.class.getClassLoader()
                        .getResourceAsStream("configuration/config.json")) {
                    if (is == null) {
                        throw new RuntimeException("Default config file not found in JAR resources");
                    }
                    Files.copy(is, CONFIG_PATH);
                }
            }

            final String content = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            CONFIG = new JSONObject(content);
            final int[] de = new int[]{24, 2, 100};

            final String[] requiredKeys = {"input_language", "target_language", "dark_mode",
                    "font_size", "speed", "increment", "original_script", "page_length",
                    "logs", "font", "api_key", "credentials_path", "pages_translated"
            };

            final Object[] defaultValues = {"en", "fr", "false", de[0], de[1], true,
                    true, de[2], "none", "times new roman", "none", "path", 3,
            };

            for (int i = 0; i < requiredKeys.length; i++) {
                if (!CONFIG.has(requiredKeys[i])) {
                    CONFIG.put(requiredKeys[i], defaultValues[i]);
                }
            }

            saveConfig();
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to load or create config: " + ex.getMessage(), ex);
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
        return CONFIG.getString(key);
    }

    /**
     * Retrieves an int value by key.
     *
     * @param key the config key
     * @return the associated int value
     */
    public static int getInt(final String key) {
        return CONFIG.getInt(key);
    }

    /**
     * Retrieves a boolean value by key.
     *
     * @param key the config key
     * @return true if value exists and is true, false otherwise
     */
    public static boolean getBool(final String key) {
        try {
            return CONFIG.getBoolean(key);
        }
        catch (NoSuchElementException ex) {
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
        CONFIG.put(key, value);
    }

    /**
     * Retrieves the configured translation speed.
     *
     * @return the speed
     */
    public static int getSpeed() {
        return CONFIG.getInt("speed");
    }

    /**
     * Saves the config file to disk.
     */
    public static void saveConfig() {
        try (BufferedWriter writer =
                     Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            writer.write(CONFIG.toString(4));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file: " + e.getMessage(), e);
        }
    }
}
