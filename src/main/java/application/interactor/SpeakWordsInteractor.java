package application.interactor;

import java.util.List;
import java.util.Objects;

import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;

/**
 * Interactor for the "Speak Words" use case.
 *
 * <p>This class belongs to the <b>application</b> layer and depends only on the
 * domain {@link Speaker} gateway. It contains no references to infrastructure
 * classes or SDKs and performs no UI or I/O logic.
 *
 * <p>All speaking operations are delegated to the injected {@code Speaker}
 * implementation. Helper methods are provided to keep existing call sites
 * working (e.g., speaking a word pair),
 * while still honoring Clean Architecture.
 */
public final class SpeakWordsInteractor implements SpeakWordsUseCase {

    /** Domain gateway for text-to-speech. */
    private final Speaker speaker;

    /**
     * Creates a new interactor with the required domain gateway.
     *
     * @param speakerGateway the {@link Speaker}
     *                       gateway; must not be {@code null}
     * @throws NullPointerException if {@code speakerGateway} is {@code null}
     */
    public SpeakWordsInteractor(final Speaker speakerGateway) {
        this.speaker = Objects.requireNonNull(speakerGateway,
                "speaker must not be null");
    }

    /**
     * Speaks a single word using the gateway's default language settings.
     *
     * @param word the word to speak; ignored if {@code null} or blank
     */
    @Override
    public void speak(final String word) {
        speaker.speak(word);
    }

    /**
     * Speaks a single word using the specified language code.
     *
     * @param word         the word to speak; ignored if {@code null} or blank
     * @param languageCode a BCP-47 language code (e.g., {@code "en-US"})
     */
    @Override
    public void speak(final String word, final String languageCode) {
        speaker.speak(word, languageCode);
    }

    /**
     * Speaks a list of words using the gateway's default language settings.
     *
     * @param words list of words; {@code null}/blank entries are ignored
     */
    @Override
    public void speakWords(final List<String> words) {
        speaker.speak(words);
    }

    /**
     * Speaks a list of words using the specified language code.
     *
     * @param words        list of words; {@code null}/blank entries are ignored
     * @param languageCode a BCP-47 language code (e.g., {@code "fr-FR"})
     */
    @Override
    public void speakWords(final List<String> words,
                           final String languageCode) {
        speaker.speak(words, languageCode);
    }

    /**
     * Convenience helper to speak an original word followed by its translation.
     *
     * @param original   the original word (may be {@code null}/blank)
     * @param translated the translated word (may be {@code null}/blank)
     */
    public void speakWordPair(final String original, final String translated) {
        speaker.speak(original);
        speaker.speak(translated);
    }

    /**
     * Convenience helper to speak a single word in a specific language.
     *
     * @param word         the word to speak
     * @param languageCode a BCP-47 language code (e.g., {@code "en-GB"})
     */
    public void speakWord(final String word, final String languageCode) {
        speaker.speak(word, languageCode);
    }
}
