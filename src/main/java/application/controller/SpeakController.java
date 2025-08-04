package application.controller;

import application.interactor.SpeakWordsInteractor;
import application.usecase.SpeakWordsUseCase;

import java.util.List;

public class SpeakController {

    private final SpeakWordsUseCase useCase;
    private final domain.gateway.Speaker speaker;

    public SpeakController(SpeakWordsUseCase useCase, domain.gateway.Speaker speaker) {
        this.useCase = useCase;
        this.speaker = speaker;
    }


    public void speak(String word) {
        useCase.speak(word);
    }

    public void speakWords(List<String> words) {
        useCase.speakWords(words);
    }

    public void speakWordPair(String original, String translated) {
        if (useCase instanceof SpeakWordsInteractor) {
            ((SpeakWordsInteractor) useCase).speakWordPair(original, translated);
        }
    }

    public void speakWord(String word, String languageCode) {
        if (useCase instanceof SpeakWordsInteractor) {
            ((SpeakWordsInteractor) useCase).speakWord(word, languageCode);
        }
    }
    public boolean isTTSAvailable() {
        return speaker != null
                && speaker instanceof infrastructure.tts.SpeechManager
                && ((infrastructure.tts.SpeechManager) speaker).isAvailable();
    }

}
