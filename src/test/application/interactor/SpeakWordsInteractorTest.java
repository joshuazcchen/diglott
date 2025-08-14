package application.interactor;

import domain.gateway.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

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
    void testSpeakWordPair_callsOriginalThenTranslated() {
        interactor.speakWordPair("hello", "bonjour");

        InOrder inOrder = inOrder(mockSpeaker);
        inOrder.verify(mockSpeaker).speak("hello");
        inOrder.verify(mockSpeaker).speak("bonjour");
        verifyNoMoreInteractions(mockSpeaker);
    }

    @Test
    void testSpeakWordWithLanguage_delegatesToGateway() {
        interactor.speak("hola", "es");
        verify(mockSpeaker).speak("hola", "es");
        verifyNoMoreInteractions(mockSpeaker);
    }

    @Test
    void testSpeak_withNull_passthrough() {
        interactor.speak(null);
        verify(mockSpeaker).speak((String) isNull());
    }

    @Test
    void testSpeakWordPair_safeFallback_noThrow() {
        assertDoesNotThrow(() -> interactor.speakWordPair("hi", "salut"));
    }
}
