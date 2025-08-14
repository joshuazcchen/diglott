package application.usecase;

import java.util.List;

/**
 * Use case boundary for speaking words.
 * <p>
 * This boundary is consumed by controllers and implemented by an interactor
 * that depends only on the domain {@code Speaker} gateway.
 */
public interface SpeakWordsUseCase {

    /**
     * Speak a single word using the default language configuration.
     *
     * @param word the word to speak
     */
    void speak(String word);

    /**
     * Speak a single word in a provided language.
     *
     * @param word         the word to speak
     * @param languageCode BCP-47 language code (e.g., "en-US")
     */
    void speak(String word, String languageCode);

    /**
     * Speak a list of words using the default language configuration.
     *
     * @param words list of words to speak
     */
    void speakWords(List<String> words);

    /**
     * Speak a list of words in a provided language.
     *
     * @param words        list of words to speak
     * @param languageCode BCP-47 language code (e.g., "fr-FR")
     */
    void speakWords(List<String> words, String languageCode);
}
