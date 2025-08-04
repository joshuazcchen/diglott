package application.interactor;

import domain.gateway.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class SpeakWordsInteractorTest {

    private Speaker mockSpeaker;
    private SpeakWordsInteractor interactor;

    @BeforeEach
    void setUp() {
        mockSpeaker = mock(Speaker.class);
        interactor = new SpeakWordsInteractor(mockSpeaker);
    }

    @Test
    void testSpeakSingleWord() {
        interactor.speak("hello");
        verify(mockSpeaker).speak("hello");
    }

    @Test
    void testSpeakWordList() {
        List<String> words = List.of("bonjour", "salut");
        interactor.speakWords(words);
        verify(mockSpeaker).speak(words);
    }

    @Test
    void testSpeakWordPair_WhenSpeakerIsNotSpeechManager() {
        // Should not throw error, but nothing happens
        interactor.speakWordPair("hello", "bonjour");
        verify(mockSpeaker, never()).speak(anyString());
    }

    @Test
    void testSpeakWordWithLanguage_WhenSpeakerIsNotSpeechManager() {
        interactor.speakWord("hola", "es");
        verify(mockSpeaker, never()).speak(anyString());
    }

    @Test
    void testSpeak_withNull() {
        interactor.speak(null);
        verify(mockSpeaker).speak((String) isNull());
    }

    @Test
    void testSpeakWordPair_safeFallback() {
        assertDoesNotThrow(() -> interactor.speakWordPair("hi", "salut"));
    }

}
