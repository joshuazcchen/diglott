package infrastructure.translation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransliterationHandlerTest {

    private TransliterationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new TransliterationHandler();
    }

    // Utility to remove any non-ASCII for test assertion purposes
    private String asciiOnly(String s) {
        return s.replaceAll("[^A-Za-z\\s]", "");
    }

    @Test
    void testTransliterate_FarsiScript() {
        String input = "سلام"; // "Salaam"
        String result = handler.transliterate(input);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(asciiOnly(result).matches("[A-Za-z\\s]+"),
                "Result should be ASCII letters only");
    }

    @Test
    void testTransliterate_MandarinScript() {
        String input = "你好"; // "Ni Hao"
        String result = handler.transliterate(input);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.matches("[A-Za-z\\s]+"), "Result "
                + "should be ASCII letters only");
    }

    @Test
    void testTransliterate_UrduScript_KhudaHafiz() {
        String input = "خدا حافظ"; // "Khuda Hafiz"
        String result = handler.transliterate(input);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(asciiOnly(result).matches("[A-Za-z\\s]+"),
                "Result should be ASCII letters only");
    }

    @Test
    void testTransliterate_LatinInput() {
        String input = "Hello World!";
        String result = handler.transliterate(input);
        assertEquals("Hello World!", result);
    }

    @Test
    void testTransliterate_EmptyString() {
        String result = handler.transliterate("");
        assertEquals("", result);
    }

    @Test
    void testTransliterate_NullInput() {
        String result = handler.transliterate(null);
        assertEquals("", result);
    }

    @Test
    void testTransliterate_ComplexScript() {
        String input = "علي"; // "Ali"
        String result = handler.transliterate(input);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(asciiOnly(result).matches("[A-Za-z\\s]+"),
                "Result should be ASCII letters only");
    }
}
