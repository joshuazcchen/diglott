package domain.gateway;

/**
 * Gateway interface for translating words.
 */
public interface Translator {

    /**
     * Adds a translated version of the specified word to the translation store.
     *
     * @param word the word to translate and store
     * @throws Exception if translation fails or cannot be performed
     */
    void addWord(String word) throws Exception;
}
