package application.interactor;

import application.usecase.SpeakWordsUseCase;
import domain.gateway.Speaker;

import java.util.List;

public class SpeakWordsInteractor implements SpeakWordsUseCase {
    private final Speaker speaker;

    public SpeakWordsInteractor(Speaker speaker) {
        this.speaker = speaker;
    }

    @Override
    public void speakWords(List<String> words) {
        speaker.speak(words);
    }
}
