package application.interactor;

import java.util.List;

import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;
import infrastructure.tts.SpeechManager;

/**
 * Interactor for speaking words using the configured {@link Speaker}.
 * <p>
 * Delegates all speaking operations to the provided {@code Speaker}
 * implementation, including support for speaking translated word pairs
 * or language-specific pronunciations when supported.
 */
public final class SpeakWordsInteractor implements SpeakWordsUseCase {

    /**
     * The speaker implementation used to perform speech operations.
     * Can be a basic speaker or an advanced one like {@link SpeechManager}.
     */
    private final Speaker speaker;

    /**
     * Constructs a new SpeakWordsInteractor with the given speaker.
     *
     * @param speak the speaker implementation to use
     */
    public SpeakWordsInteractor(final Speaker speak) {
        this.speaker = speak;
    }

    /**
     * Speaks a single word using the speaker.
     *
     * @param word the word to speak
     */
    @Override
    public void speak(final String word) {
        speaker.speak(word);
    }

    /**
     * Speaks a list of words using the speaker.
     *
     * @param words the list of words to speak
     */
    @Override
    public void speakWords(final List<String> words) {
        speaker.speak(words);
    }

    /**
     * Speaks a pair of words, typically an original word and its translation.
     * <p>
     * This method only works if the speaker
     * is an instance of {@link SpeechManager}.
     *
     * @param original   the original word to speak
     * @param translated the translated version of the word to speak
     */
    public void speakWordPair(final String original, final String translated) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWordPair(original, translated);
        }
    }

    /**
     * Speaks a word in a specific language.
     * <p>
     * This method only works if the speaker
     * is an instance of {@link SpeechManager}.
     *
     * @param word         the word to speak
     * @param languageCode the language code to use (e.g. "en", "fr")
     */
    public void speakWord(final String word, final String languageCode) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWord(word, languageCode);
        }
    }
}
