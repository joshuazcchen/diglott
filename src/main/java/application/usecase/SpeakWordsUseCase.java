package application.usecase;

import java.util.List;

public interface SpeakWordsUseCase {
    void speakWords(List<String> words);
    void speak(String word);
}
