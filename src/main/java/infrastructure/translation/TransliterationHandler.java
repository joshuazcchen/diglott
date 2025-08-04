package infrastructure.translation;

import com.ibm.icu.text.Transliterator;
import domain.gateway.WordTransliterator;

/**
 * Handles the transliteration of text from any script to Latin ASCII.
 */
public class TransliterationHandler implements WordTransliterator {

    private final Transliterator transliterator;

    /**
     * Creates a transliteration handler using the ICU4J transliterator
     * configured to convert any script to Latin ASCII.
     */
    public TransliterationHandler() {
        this.transliterator = Transliterator.getInstance("Any-Latin; Latin-ASCII");
    }

    @Override
    public String transliterate(final String input) {
        if (input == null) {
            return "";
        }
        return transliterator.transliterate(input);
    }
}
