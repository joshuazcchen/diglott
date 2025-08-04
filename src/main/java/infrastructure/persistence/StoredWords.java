package infrastructure.persistence;

import Configuration.ConfigDataRetriever;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores translated words in memory during application runtime.
 */
public class StoredWords {

    /**
     * Stores mappings from the original word to its translated form.
     */
    private final Map<String, String> translated;

    /**
     * Creates a new instance of {@code StoredWords} with an empty translations map.
     */
    public StoredWords() {
        this.translated = new HashMap<>();
    }

    /**
     * Adds a translation to the map.
     *
     * @param key   the original word
     * @param value the translated word
     */
    public void addTranslation(final String key, final String value) {
        if ("debug".equals(ConfigDataRetriever.get("logs"))) {
            System.out.println(key + " " + value.toLowerCase());
        }
        translated.put(key, value.toLowerCase());
    }

    /**
     * Retrieves the current map of stored translations.
     *
     * @return a map of original words to their translated forms
     */
    public Map<String, String> getTranslations() {
        return translated;
    }
}
