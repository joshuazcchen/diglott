package application.interactor;

import java.util.List;

import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;
import infrastructure.tts.SpeechManager;

public class SpeakWordsInteractor implements SpeakWordsUseCase {

    private final Speaker speaker;

    public SpeakWordsInteractor(Speaker speaker) {
        this.speaker = speaker;
    }

    @Override
    public void speak(String word) {
        speaker.speak(word);
    }

    @Override
    public void speakWords(List<String> words) {
        speaker.speak(words);
    }

    /**
     * Speaks a pair of words, typically an original word and its translation.
     *
     * @param original the original word to speak
     * @param translated the translated version of the word to speak
     */
    public void speakWordPair(String original, String translated) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWordPair(original, translated);
        }
    }

    /**
     * Speaks a word in the specified language.
     *
     * @param word the word to speak
     * @param languageCode the language code to use for speaking the word
     */
    public void speakWord(String word, String languageCode) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWord(word, languageCode);
        }
    }
}
