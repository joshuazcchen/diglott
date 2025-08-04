package application.interactor;

import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;
import infrastructure.tts.SpeechManager;

import java.util.List;

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

    public void speakWordPair(String original, String translated) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWordPair(original, translated);
        }
    }

    public void speakWord(String word, String languageCode) {
        if (speaker instanceof SpeechManager sm) {
            sm.speakWord(word, languageCode);
        }
    }
}
