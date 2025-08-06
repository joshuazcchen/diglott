package application.controller;

import java.util.List;

import application.interactor.SpeakWordsInteractor;
import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;
import infrastructure.tts.SpeechManager;

/**
 * Controller responsible for coordinating text-to-speech actions.
 * Delegates word or phrase speaking operations to the configured
 * {@link SpeakWordsUseCase}.
 */
public final class SpeakController {

    /** Use case responsible for speaking words. */
    private final SpeakWordsUseCase speakUseCase;

    /** Speaker gateway for checking TTS availability. */
    private final Speaker speakGateway;

    /**
     * Constructs a SpeakController with the provided use case and speaker.
     *
     * @param speakU  the use case to execute speaking actions
     * @param speakG  the gateway for accessing speech capabilities
     */
    public SpeakController(
            final SpeakWordsUseCase speakU,
            final Speaker speakG
    ) {
        this.speakUseCase = speakU;
        this.speakGateway = speakG;
    }

    /**
     * Speaks a single word using the configured use case.
     *
     * @param word the word to speak
     */
    public void speak(final String word) {
        speakUseCase.speak(word);
    }

    /**
     * Speaks a list of words using the configured use case.
     *
     * @param words the list of words to speak
     */
    public void speakWords(final List<String> words) {
        speakUseCase.speakWords(words);
    }

    /**
     * Speaks a pair of words (original and translation),
     * if the use case supports it.
     *
     * @param original   the original word
     * @param translated the translated word
     */
    public void speakWordPair(
            final String original,
            final String translated
    ) {
        if (speakUseCase instanceof SpeakWordsInteractor interactor) {
            interactor.speakWordPair(original, translated);
        }
    }

    /**
     * Speaks a word in a specific language,
     * if the use case supports it.
     *
     * @param word         the word to speak
     * @param languageCode the language code (e.g. "en", "fr")
     */
    public void speakWord(
            final String word,
            final String languageCode
    ) {
        if (speakUseCase instanceof SpeakWordsInteractor interactor) {
            interactor.speakWord(word, languageCode);
        }
    }

    /**
     * Checks if text-to-speech functionality is available.
     *
     * @return true if available, false otherwise
     */
    public boolean isTtsAvailable() {
        return speakGateway instanceof SpeechManager manager
                && manager.isAvailable();
    }
}
