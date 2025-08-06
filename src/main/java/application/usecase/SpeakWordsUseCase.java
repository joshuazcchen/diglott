package application.usecase;

import java.util.List;

/**
 * Interface defining operations for speaking words.
 */
public interface SpeakWordsUseCase {

    /**
     * Speaks a list of words.
     *
     * @param words the list of words to speak
     */
    void speakWords(List<String> words);

    /**
     * Speaks a single word.
     *
     * @param word the word to speak
     */
    void speak(String word);
}
