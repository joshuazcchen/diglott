package configuration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.json.JSONObject;

/**
 * Utility class for loading and accessing Diglott configuration
 * values from a JSON file stored in the user's home directory.
 */
public final class ConfigDataRetriever {

    /**
     * Path to the configuration file stored in the user's home directory.
     * Used to load and persist application settings.
     */
    private static final Path CONFIG_PATH = Paths.get(
            System.getProperty("user.home"), ".diglott", "config.json"
    );

    /** Number of spaces used to indent JSON when saving. */
    private static final int JSON_INDENT = 4;

    /** Default font size for displaying text. */
    private static final int DEFAULT_FONT_SIZE = 24;

    /** Default translation speed level. */
    private static final int DEFAULT_SPEED = 2;

    /** Default maximum number of words per page. */
    private static final int DEFAULT_PAGE_LENGTH = 100;

    /** Default number of pages translated initially. */
    private static final int DEFAULT_PAGES_TRANSLATED = 3;

    /** In-memory configuration object loaded from JSON. */
    private static final JSONObject CONFIG;

    static {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());

                try (InputStream is = ConfigDataRetriever.class
                        .getClassLoader()
                        .getResourceAsStream("configuration/config.json")) {
                    if (is == null) {
                        throw new IllegalStateException(
                                "Default config file not"
                                        + "found in JAR resources."
                        );
                    }
                    Files.copy(is, CONFIG_PATH);
                }
            }

            final String content = Files.readString(
                    CONFIG_PATH, StandardCharsets.UTF_8
            );
            CONFIG = new JSONObject(content);

            initializeDefaults();
            saveConfig();

        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load config.", ex);
        }
    }

    /**
     * Prevents instantiation of utility class.
     */
    private ConfigDataRetriever() {
        // Not instantiable
    }

    /**
     * Ensures all required configuration keys exist,
     * initializing missing values with defaults.
     */
    private static void initializeDefaults() {
        final String[] requiredKeys = {
                "input_language", "target_language", "dark_mode",
                "font_size", "speed", "increment", "original_script",
                "page_length", "logs", "font", "deepl_api_key",
                "credentials_path", "pages_translated",
                "azure_api_key", "azure_region"
        };

        final Object[] defaultValues = {
                "en", "fr", "false", DEFAULT_FONT_SIZE, DEFAULT_SPEED, true,
                true, DEFAULT_PAGE_LENGTH, "none", "times new roman",
                "none", "path", DEFAULT_PAGES_TRANSLATED, "none", "canadaeast",
        };

        for (int i = 0; i < requiredKeys.length; i++) {
            if (!CONFIG.has(requiredKeys[i])) {
                CONFIG.put(requiredKeys[i], defaultValues[i]);
            }
        }
    }

    /**
     * Retrieves a string value by config key.
     *
     * @param key the config key
     * @return the associated string value
     */
    public static String get(final String key) throws NoSuchElementException {
        return CONFIG.getString(key);
    }

    /**
     * Retrieves an integer value by config key.
     *
     * @param key the config key
     * @return the associated integer value
     */
    public static int getInt(final String key) {
        return CONFIG.getInt(key);
    }

    /**
     * Retrieves a boolean value by config key.
     *
     * @param key the config key
     * @return true if value exists and is true, false otherwise
     */
    public static boolean getBool(final String key) {
        try {
            return CONFIG.getBoolean(key);
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Updates a configuration key to the specified value.
     * Changes are in-memory only unless {@link #saveConfig()} is called.
     *
     * @param key   the key to update
     * @param value the new value
     */
    public static void set(final String key, final Object value) {
        CONFIG.put(key, value);
    }

    /**
     * Retrieves the configured translation speed level.
     *
     * @return the speed
     */
    public static int getSpeed() {
        return CONFIG.getInt("speed");
    }

    /**
     * Saves the current configuration to disk.
     * Overwrites the existing config file.
     */
    public static void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(
                CONFIG_PATH, StandardCharsets.UTF_8)) {
            writer.write(CONFIG.toString(JSON_INDENT));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save config file.", e);
        }
    }
}
