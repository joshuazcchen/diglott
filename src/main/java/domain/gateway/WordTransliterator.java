package domain.gateway;

/**
 * Gateway interface for transliterating text from one script to another.
 */
public interface WordTransliterator {

    /**
     * Transliterates the given input string.
     *
     * @param input the original text to transliterate
     * @return the transliterated version of the input
     */
    String transliterate(String input);
}
