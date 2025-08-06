package infrastructure.translation;

import com.ibm.icu.text.Transliterator;
import domain.gateway.WordTransliterator;

/**
 * Handles the transliteration of text from any script
 * to Latin ASCII using ICU4J.
 */
public class TransliterationHandler implements WordTransliterator {

    /**
     * ICU4J transliterator for "Any-Latin; Latin-ASCII".
     */
    private final Transliterator transliterator;

    /**
     * Creates a transliteration handler configured to convert
     * any script to Latin ASCII.
     */
    public TransliterationHandler() {
        this.transliterator =
                Transliterator.getInstance("Any-Latin; Latin-ASCII");
    }

    /**
     * Transliterates the given input string into Latin ASCII.
     *
     * @param input the input string to transliterate
     * @return the transliterated result, or empty string if input is null
     */
    @Override
    public String transliterate(final String input) {
        if (input == null) {
            return "";
        }
        return transliterator.transliterate(input);
    }
}
