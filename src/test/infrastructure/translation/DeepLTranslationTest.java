package infrastructure.translation;

import infrastructure.persistence.StoredWords;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeepLTranslationHandlerTest {

    private StoredWords mockStorage;
    private DeepLTranslationHandler handler;

    @BeforeEach
    void setUp() {
        mockStorage = mock(StoredWords.class);
        // Use a non-empty key so we don't hit ConfigDataRetriever
        handler = new DeepLTranslationHandler("test-key", mockStorage);
    }

    @Test
    void testAddWord_StoresTranslation_WhenApiSucceeds() throws Exception {
        String word = "house";
        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        DeepLTranslationHandler spyHandler = spy(handler);
        doReturn(new JSONObject("{\"translations\":[{\"text\":\"Haus\"}]}"))
                .when(spyHandler).makeApiCall(anyString());

        spyHandler.addWord(word);

        verify(mockStorage).addTranslation("house", "Haus");
    }

    @Test
    void testAddWord_DoesNotStore_WhenApiReturnsEmpty() throws Exception {
        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        DeepLTranslationHandler spyHandler = spy(handler);
        doReturn(new JSONObject("{\"translations\":[]}"))
                .when(spyHandler).makeApiCall(anyString());

        spyHandler.addWord("car");

        verify(mockStorage, never())
                .addTranslation(anyString(), anyString());
    }

    @Test
    void testAddWord_ThrowsException_WhenApiCallFails() throws Exception {
        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        DeepLTranslationHandler spyHandler = spy(handler);
        doThrow(new RuntimeException("API error"))
                .when(spyHandler).makeApiCall(anyString());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> spyHandler.addWord("tree")
        );
        assertTrue(ex.getMessage().contains("API error"));

        verify(mockStorage, never())
                .addTranslation(anyString(), anyString());
    }

    @Test
    void testAddWord_DoesNotAddDuplicateTranslation() throws Exception {
        Map<String, String> existing = new HashMap<>();
        existing.put("water", "Wasser");
        when(mockStorage.getTranslations()).thenReturn(existing);

        DeepLTranslationHandler spyHandler = spy(handler);
        doReturn(new JSONObject("{\"translations\":[{\"text\":\"Wasser\"}]}"))
                .when(spyHandler).makeApiCall(anyString());

        spyHandler.addWord("water");

        verify(mockStorage, never())
                .addTranslation(anyString(), anyString());
    }
}
