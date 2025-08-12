package infrastructure.translation;

import infrastructure.persistence.StoredWords;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DeepLTranslationHandlerTest {
    private StoredWords mockStorage;
    private DeepLTranslationHandler handler;

    @BeforeEach
    public void setUp() {
        mockStorage = mock(StoredWords.class);
        handler = new DeepLTranslationHandler("none", mockStorage);
    }

    /*
    This test verifies that the test adds the correct words when the API succeeds.
    This utilizes Mockito's verification.
     */
    @Test
    public void testAddWord_StoresTranslation_WhenApiSucceeds() throws Exception {
        String word = "house";

        Map<String, String> fakeStorage = new HashMap<>();
        when(mockStorage.getTranslations()).thenReturn(fakeStorage);

        DeepLTranslationHandler spyHandler = spy(handler);

        doReturn(new JSONObject("""
            {"translations":[{"text":"Haus"}]}
        """)).when(spyHandler).makeApiCall(anyString());
        spyHandler.addWord(word);

        verify(mockStorage).addTranslation("house", "Haus");
    }

    @Test
    public void testAddWord_DoesNotStore_WhenApiReturnsEmpty() throws Exception {
        String word = "car";

        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        DeepLTranslationHandler spyHandler = spy(handler);

        doReturn(new JSONObject("""
        {"translations":[]}
    """)).when(spyHandler).makeApiCall(anyString());

        spyHandler.addWord(word);

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }

    @Test
    public void testAddWord_ThrowsException_WhenApiCallFails() throws Exception {
        String word = "tree";

        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        DeepLTranslationHandler spyHandler = spy(handler);

        doThrow(new RuntimeException("API error")).when(spyHandler).makeApiCall(anyString());

        Exception exception = assertThrows(RuntimeException.class, () -> spyHandler.addWord(word));
        assertTrue(exception.getMessage().contains("API error"));

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }

    @Test
    public void testAddWord_DoesNotAddDuplicateTranslation() throws Exception {
        String word = "water";

        Map<String, String> existingTranslations = new HashMap<>();
        existingTranslations.put(word, "Wasser");
        when(mockStorage.getTranslations()).thenReturn(existingTranslations);

        DeepLTranslationHandler spyHandler = spy(handler);

        doReturn(new JSONObject("""
        {"translations":[{"text":"Wasser"}]}
    """)).when(spyHandler).makeApiCall(anyString());

        spyHandler.addWord(word);

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }
}
