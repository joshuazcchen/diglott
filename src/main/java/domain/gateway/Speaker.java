package domain.gateway;

import java.util.List;

public interface Speaker {
    void speak(List<String> words);
    void speak(String word);

    void speak(List<String> words, String languageCode);
    void speak(String word, String languageCode);
}
