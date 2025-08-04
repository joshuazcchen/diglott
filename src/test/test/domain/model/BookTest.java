package domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Page page1, page2, page3;
    private Book book;

    @BeforeEach
    void setUp() {
        page1 = new Page(List.of("one"), 1, 5);
        page2 = new Page(List.of("two"), 2, 5);
        page3 = new Page(List.of("three"), 3, 5);
        book = new Book(List.of(page3, page1, page2)); // out of order on purpose
    }

    @Test
    void constructorSortsPagesAndStartsAtFirstPage() {
        assertEquals(1, book.getCurrentPageNumber());
        assertEquals("one", book.getCurrentContent());
    }

    @Test
    void constructorThrowsIfPagesNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null));
        assertThrows(IllegalArgumentException.class, () -> new Book(List.of()));
    }

    @Test
    void getPageReturnsCorrectPage() {
        assertEquals("two", book.getPage(2).getContent());
        assertEquals("three", book.getPage(3).getContent());
    }

    @Test
    void getPageThrowsIfPageNotFound() {
        assertThrows(NoSuchElementException.class, () -> book.getPage(99));
    }

    @Test
    void goToPageChangesCurrentPage() {
        book.goToPage(2);
        assertEquals(2, book.getCurrentPageNumber());
        assertEquals("two", book.getCurrentContent());
    }

    @Test
    void goToPageThrowsIfInvalidPage() {
        assertThrows(NoSuchElementException.class, () -> book.goToPage(999));
    }

    @Test
    void nextPageAdvancesToNextIfExists() {
        book.goToPage(1);
        book.nextPage();
        assertEquals(2, book.getCurrentPageNumber());
    }

    @Test
    void nextPageDoesNothingIfNoNextPage() {
        book.goToPage(3);
        book.nextPage();
        assertEquals(3, book.getCurrentPageNumber()); // stays the same
    }

    @Test
    void previousPageGoesBackIfExists() {
        book.goToPage(3);
        book.previousPage();
        assertEquals(2, book.getCurrentPageNumber());
    }

    @Test
    void previousPageDoesNothingIfNoPrevious() {
        book.goToPage(1);
        book.previousPage();
        assertEquals(1, book.getCurrentPageNumber()); // stays the same
    }

    @Test
    void getAllPagesReturnsUnmodifiableSortedList() {
        List<Page> pages = book.getAllPages();
        assertEquals(3, pages.size());
        assertEquals(1, pages.get(0).getPageNumber());
        assertThrows(UnsupportedOperationException.class, () -> pages.add(page1));
    }

    @Test
    void getTotalPagesReturnsCorrectCount() {
        assertEquals(3, book.getTotalPages());
    }
}
