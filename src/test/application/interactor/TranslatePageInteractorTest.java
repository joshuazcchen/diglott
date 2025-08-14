package application.interactor;

import configuration.ConfigDataRetriever;
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import domain.model.Page;
import infrastructure.persistence.StoredWords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Mockito-style tests for TranslatePageInteractor. */
class TranslatePageInteractorTest {

    private StoredWords store;
    private Translator translator;
    private WordTransliterator transliterator;

    @BeforeEach
    void setUp() throws Exception {
        // deterministic, non-incremental to make expectations stable
        ConfigDataRetriever.set("increment", "false");
        ConfigDataRetriever.set("original_script", "false");
        ConfigDataRetriever.set("speed", "2"); // translate 2 words per page

        store = new StoredWords();
        translator = mock(Translator.class);
        transliterator = mock(WordTransliterator.class);

        // whenever transliterate is called, wrap the value
        when(transliterator.transliterate(anyString()))
                .thenAnswer(inv -> "TL(" + inv.getArgument(0, String.class) + ")");

        // simulate that adding a word also saves its translation in the store
        doAnswer(inv -> {
            String w = inv.getArgument(0, String.class);
            store.getTranslations().put(w.toLowerCase(Locale.ROOT), "tr-" + w);
            return null;
        }).when(translator).addWord(anyString());
    }

    @Test
    void execute_translatesSomeWords_andRewritesContent() throws Exception {
        TranslatePageInteractor uc =
                new TranslatePageInteractor(translator, transliterator, store);

        List<String> words = Arrays.asList("alpha", "beta", "gamma", "delta");
        Page page = new Page(words, /*pageNum*/1, /*max*/20);

        uc.execute(page);

        // verify translator asked to translate exactly "speed" words
        verify(translator, times(2)).addWord(anyString());

        // content contains markup for translated words
        String out = page.getContent();
        int highlights = count(out, "<b><u>");
        assertEquals(2, highlights);

        // transliterator used for each translated entry
        verify(transliterator, atLeast(2)).transliterate(startsWith("tr-"));
    }

    @Test
    void execute_doesNotThrow_onValidInput() {
        TranslatePageInteractor uc =
                new TranslatePageInteractor(translator, transliterator, store);

        Page page = new Page(Arrays.asList("alpha", "beta", "gamma"), 1, 20);
        assertDoesNotThrow(() -> uc.execute(page));
    }

    private static int count(String haystack, String needle) {
        int c = 0, from = 0;
        while (true) {
            int i = haystack.indexOf(needle, from);
            if (i < 0) break;
            c++; from = i + needle.length();
        }
        return c;
    }
}
