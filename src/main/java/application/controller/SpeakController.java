package application.controller;

import application.usecase.SpeakWordsUseCase;

import java.util.List;

public class SpeakController {
    private final SpeakWordsUseCase speakUseCase;

    public SpeakController(SpeakWordsUseCase speakUseCase) {
        this.speakUseCase = speakUseCase;
    }

    public void speakWords(List<String> words) {
        speakUseCase.speakWords(words);
    }
}
