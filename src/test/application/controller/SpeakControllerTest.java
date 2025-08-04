package application.controller;

import application.interactor.SpeakWordsInteractor;
import domain.gateway.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class SpeakControllerTest {

    private SpeakWordsInteractor mockUseCase;
    private Speaker mockSpeaker;
    private SpeakController controller;

    @BeforeEach
    void setUp() {
        mockUseCase = mock(SpeakWordsInteractor.class);
        mockSpeaker = mock(Speaker.class);
        controller = new SpeakController(mockUseCase, mockSpeaker);
    }

    @Test
    void testSpeakSingleWord() {
        controller.speak("hello");
        verify(mockUseCase).speak("hello");
    }

    @Test
    void testSpeakMultipleWords() {
        List<String> words = List.of("hello", "world");
        controller.speakWords(words);
        verify(mockUseCase).speakWords(words);
    }
}
