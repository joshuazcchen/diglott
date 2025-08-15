package infrastructure.translation;

import infrastructure.persistence.StoredWords;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AzureTranslationHandlerTest {
    private StoredWords mockStorage;
    private AzureTranslationHandler handler;

    @BeforeEach
    void setUp() {
        mockStorage = mock(StoredWords.class);
        // Pass dummy key/region to avoid ConfigDataRetriever in tests
        handler = new AzureTranslationHandler("testKey",
                "testRegion", mockStorage);
    }

    /*
     * Verifies that we store the translation when Azure returns one.
     */
    @Test
    void testAddWord_StoresTranslation_WhenApiSucceeds() throws Exception {
        String word = "house";
        Map<String, String> fakeStorage = new HashMap<>();
        when(mockStorage.getTranslations()).thenReturn(fakeStorage);

        AzureTranslationHandler spyHandler = spy(handler);

        // AzureTranslationHandler expects a JSONObject with "translations"
        doReturn(new JSONObject()
                .put("translations", new JSONArray()
                        .put(new JSONObject().put("text", "Haus"))))
                .when(spyHandler).makeApiCall(anyString(), anyString());

        spyHandler.addWord(word);

        verify(mockStorage).addTranslation("house", "Haus");
    }

    @Test
    void testAddWord_DoesNotStore_WhenApiReturnsEmpty() throws Exception {
        String word = "car";
        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        AzureTranslationHandler spyHandler = spy(handler);

        doReturn(new JSONObject()
                .put("translations", new JSONArray()))
                .when(spyHandler).makeApiCall(anyString(), anyString());

        spyHandler.addWord(word);

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }

    @Test
    void testAddWord_ThrowsException_WhenApiCallFails() throws Exception {
        String word = "tree";
        when(mockStorage.getTranslations()).thenReturn(new HashMap<>());

        AzureTranslationHandler spyHandler = spy(handler);

        doThrow(new RuntimeException("API error"))
                .when(spyHandler).makeApiCall(anyString(), anyString());

        Exception ex = assertThrows(RuntimeException.class, () ->
                spyHandler.addWord(word));
        assertTrue(ex.getMessage().contains("API error"));

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }

    @Test
    void testAddWord_DoesNotAddDuplicateTranslation() throws Exception {
        String word = "water";
        Map<String, String> existingTranslations = new HashMap<>();
        existingTranslations.put(word.toLowerCase(), "Wasser");
        when(mockStorage.getTranslations()).thenReturn(existingTranslations);

        AzureTranslationHandler spyHandler = spy(handler);

        doReturn(new JSONObject()
                .put("translations", new JSONArray()
                        .put(new JSONObject().put("text", "Wasser"))))
                .when(spyHandler).makeApiCall(anyString(), anyString());

        spyHandler.addWord(word);

        verify(mockStorage, never()).addTranslation(anyString(), anyString());
    }
}
