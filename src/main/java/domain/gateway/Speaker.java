package domain.gateway;

import java.util.List;

/**
 * Interface for components that can speak words.
 */
public interface Speaker {
    /**
     * Speaks a list of words.
     *
     * @param words the list of words to speak
     */
    void speak(List<String> words);

    /**
     * Speaks a single word.
     *
     * @param word the word to speak
     */
    void speak(String word);

    /**
     * Speaks a list of words in the specified language.
     *
     * @param words the list of words to speak
     * @param languageCode the language code (e.g., "en", "fr")
     */
    void speak(List<String> words, String languageCode);

    /**
     * Speaks a single word in the specified language.
     *
     * @param word the word to speak
     * @param languageCode the language code (e.g., "en", "fr")
     */
    void speak(String word, String languageCode);
}
