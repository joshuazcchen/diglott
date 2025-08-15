package infrastructure.tts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SpeechManager}.
 */
class SpeechManagerTest {

    @Test
    void isAvailable_False_WhenNoCredentialsProvided() {
        final SpeechManager mgrEmpty = new SpeechManager("");
        final SpeechManager mgrNull = new SpeechManager(null);

        assertFalse(mgrEmpty.isAvailable(), "TTS should be disabled.");
        assertFalse(mgrNull.isAvailable(), "TTS should be disabled.");
    }

    @Test
    void speak_FiltersWords_AndNormalizesLanguage() {
        final TestableSpeechManager mgr = new TestableSpeechManager();

        // Use Arrays.asList to allow null in the list.
        final List<String> input =
                Arrays.asList("hi", "", "  ", null, "bye");

        mgr.speak(input, "en-US");

        assertEquals(2, mgr.spokenWordCalls.size(),
                "Only non-blank words should be spoken.");
        assertEquals("hi", mgr.spokenWordCalls.get(0).word);
        assertEquals("bye", mgr.spokenWordCalls.get(1).word);
        assertEquals("en-US", mgr.spokenWordCalls.get(0).lang);
        assertEquals("en-US", mgr.spokenWordCalls.get(1).lang);
    }

    @Test
    void speak_NormalizesNullLanguage_ToDefault() {
        final TestableSpeechManager mgr = new TestableSpeechManager();

        mgr.speak(Arrays.asList("x"), null);

        assertEquals(1, mgr.spokenWordCalls.size());
        assertEquals("x", mgr.spokenWordCalls.get(0).word);
        assertEquals("en-US", mgr.spokenWordCalls.get(0).lang);
    }

    @Test
    void speak_NormalizesBlankLanguage_ToDefault() {
        final TestableSpeechManager mgr = new TestableSpeechManager();

        mgr.speak(Arrays.asList("x"), "  ");

        assertEquals(1, mgr.spokenWordCalls.size());
        assertEquals("x", mgr.spokenWordCalls.get(0).word);
        assertEquals("en-US", mgr.spokenWordCalls.get(0).lang);
    }

    /** Test double to intercept calls without audio/network. */
    private static final class TestableSpeechManager
            extends SpeechManager {

        private final java.util.ArrayList<WordLang> spokenWordCalls =
                new java.util.ArrayList<>();

        TestableSpeechManager() {
            super(""); // disables real TTS client
        }

        @Override
        public void speak(final String word, final String languageCode) {
            // no-op: only verify speakWord invocations
        }

        @Override
        public void speakWord(final String word,
                              final String languageCode) {
            spokenWordCalls.add(new WordLang(word, languageCode));
        }
    }

    /** Simple immutable pair of word and language code. */
    private static final class WordLang {
        private final String word;
        private final String lang;

        WordLang(final String w, final String l) {
            this.word = w;
            this.lang = l;
        }
    }
}
