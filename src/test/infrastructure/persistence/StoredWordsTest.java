package infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StoredWords}.
 */
class StoredWordsTest {

    /**
     * Verifies that a new {@code StoredWords} starts with an empty map.
     */
    @Test
    void constructor_InitializesEmptyMap() {
        final StoredWords store = new StoredWords();

        final Map<String, String> translations = store.getTranslations();

        assertTrue(translations.isEmpty(),
                "Expected translations to start empty.");
    }

    /**
     * Verifies that {@code addTranslation} stores values lowercased.
     * Also ensures keys are preserved as provided.
     */
    @Test
    void addTranslation_LowersValueCaseOnly() {
        final StoredWords store = new StoredWords();

        store.addTranslation("Hello", "BonJour");

        final Map<String, String> translations = store.getTranslations();

        assertEquals("bonjour", translations.get("Hello"),
                "Value should be stored in lowercase.");
        assertTrue(translations.containsKey("Hello"),
                "Original key should be preserved.");
    }

    /**
     * Verifies that adding a translation for an existing key overwrites the
     * previous value.
     */
    @Test
    void addTranslation_OverwritesExistingKey() {
        final StoredWords store = new StoredWords();

        store.addTranslation("cat", "Gato");
        store.addTranslation("cat", "CHAT");

        final Map<String, String> translations = store.getTranslations();

        assertEquals("chat", translations.get("cat"),
                "Latest value should overwrite the old one.");
        assertEquals(1, translations.size(),
                "Map should contain a single entry for the key.");
    }
}
