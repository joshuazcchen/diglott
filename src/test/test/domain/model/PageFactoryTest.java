package domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageFactoryTest {

    @Test
    void paginateSplitsWordsCorrectlyIntoPages() {
        List<String> words = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            words.add("word" + i);
        }

        List<Page> pages = PageFactory.paginate(words, 4);

        assertEquals(3, pages.size(), "Should create 3 pages");

        assertEquals(List.of("word1", "word2", "word3", "word4"), pages.get(0).getWords());
        assertEquals(List.of("word5", "word6", "word7", "word8"), pages.get(1).getWords());
        assertEquals(List.of("word9", "word10"), pages.get(2).getWords());

        assertEquals(1, pages.get(0).getPageNumber());
        assertEquals(2, pages.get(1).getPageNumber());
        assertEquals(3, pages.get(2).getPageNumber());
    }

    @Test
    void paginateThrowsExceptionWhenMaxWordsIsZero() {
        List<String> words = List.of("a", "b", "c");
        assertThrows(IllegalArgumentException.class, () -> PageFactory.paginate(words, 0));
    }

    @Test
    void paginateReturnsEmptyListForEmptyInput() {
        List<Page> pages = PageFactory.paginate(new ArrayList<>(), 5);
        assertTrue(pages.isEmpty());
    }

    @Test
    void paginateWithExactDivision() {
        List<String> words = List.of("a", "b", "c", "d");
        List<Page> pages = PageFactory.paginate(words, 2);

        assertEquals(2, pages.size());
        assertEquals(List.of("a", "b"), pages.get(0).getWords());
        assertEquals(List.of("c", "d"), pages.get(1).getWords());
    }
}
