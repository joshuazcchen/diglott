package application.controller;

import application.interactor.SpeakWordsInteractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class SpeakControllerTest {

    private SpeakWordsInteractor mockUseCase;
    private SpeakController controller;

    @BeforeEach
    void setUp() {
        mockUseCase = mock(SpeakWordsInteractor.class);
        controller = new SpeakController(mockUseCase);
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
