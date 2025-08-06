package application.controller;

import java.util.List;

import application.interactor.SpeakWordsInteractor;
import application.usecase.SpeakWordsUseCase;

public class SpeakController {

    private final SpeakWordsUseCase useCase;
    private final domain.gateway.Speaker speaker;

    public SpeakController(SpeakWordsUseCase useCase, domain.gateway.Speaker speaker) {
        this.useCase = useCase;
        this.speaker = speaker;
    }

    /**
     * Speaks a single word.
     * @param word the word to speak
     */
    public void speak(String word) {
        useCase.speak(word);
    }

    /**
     * Speaks a list of words.
     * @param words the list of words to speak
     */
    public void speakWords(List<String> words) {
        useCase.speakWords(words);
    }

    /**
     * Speaks a pair of words, typically an original and its translation.
     * @param original the original word
     * @param translated the translated word
     */
    public void speakWordPair(String original, String translated) {
        if (useCase instanceof SpeakWordsInteractor) {
            ((SpeakWordsInteractor) useCase).speakWordPair(original, translated);
        }
    }

    /**
     * Speaks a word in the specified language.
     * @param word the word to speak
     * @param languageCode the language code
     */
    public void speakWord(String word, String languageCode) {
        if (useCase instanceof SpeakWordsInteractor) {
            ((SpeakWordsInteractor) useCase).speakWord(word, languageCode);
        }
    }

    /**
     * Checks if text-to-speech functionality is available.
     * @return true if TTS is available, false otherwise
     */
    public boolean isTtsAvailable() {
        return speaker != null
                && speaker instanceof infrastructure.tts.SpeechManager
                && ((infrastructure.tts.SpeechManager) speaker).isAvailable();
    }
}
