package domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Page class.
 */
class PageTest {

    @Test
    void constructorValidContentCreatesPageCorrectly() {
        List<String> content = List.of("one", "two", "three");
        Page page = new Page(content, 1, 5);

        assertEquals(1, page.getPageNumber());
        assertEquals(List.of("one", "two", "three"), page.getWords());
        assertEquals("one two three", page.getContent());
        assertEquals(content, page.getOriginalWords());
        assertFalse(page.isTranslated());
    }

    @Test
    void constructorThrowsIfContentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Page(null, 1, 5));
    }

    @Test
    void constructorThrowsIfContentIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Page(List.of(), 1, 5));
    }

    @Test
    void constructorThrowsIfContentExceedsMaxWords() {
        List<String> words = List.of("a", "b", "c", "d");
        assertThrows(IllegalArgumentException.class, () -> new Page(words, 1, 2));
    }

    @Test
    void constructorThrowsIfMaxWordsIsNonPositive() {
        assertThrows(IllegalArgumentException.class, () -> new Page(List.of("a"), 1, 0));
    }

    @Test
    void rewriteTranslatedContentReplacesWordsCorrectly() {
        Page page = new Page(List.of("original"), 1, 3);
        page.rewriteTranslatedContent(List.of("new", "text"));

        assertEquals("new text", page.getContent());
        assertEquals(List.of("new", "text"), page.getWords());
        assertTrue(page.isTranslated());
    }

    @Test
    void rewriteTranslatedContentThrowsIfTooManyWords() {
        Page page = new Page(List.of("a", "b"), 1, 2);
        assertThrows(IllegalArgumentException.class,
                () -> page.rewriteTranslatedContent(List.of("a", "b", "c")));
    }

    @Test
    void rewriteTranslatedContentThrowsIfNull() {
        Page page = new Page(List.of("a"), 1, 2);
        assertThrows(IllegalArgumentException.class,
                () -> page.rewriteTranslatedContent(null));
    }

    @Test
    void resetToOriginalRestoresOriginalWords() {
        Page page = new Page(List.of("original"), 1, 3);
        page.rewriteTranslatedContent(List.of("changed"));
        assertTrue(page.isTranslated());

        page.resetToOriginal();
        assertEquals(List.of("original"), page.getWords());
        assertFalse(page.isTranslated());
    }

    @Test
    void getWordsReturnsUnmodifiableList() {
        Page page = new Page(List.of("a", "b"), 1, 5);
        List<String> words = page.getWords();
        assertThrows(UnsupportedOperationException.class, () -> words.add("c"));
    }

    @Test
    void getOriginalWordsReturnsUnmodifiableList() {
        Page page = new Page(List.of("a", "b"), 1, 5);
        List<String> original = page.getOriginalWords();
        assertThrows(UnsupportedOperationException.class, () -> original.add("c"));
    }
}
