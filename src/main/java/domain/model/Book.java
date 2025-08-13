package domain.model;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a book composed of ordered pages.
 */
public class Book {
    /**
     * The list of all pages in the book, sorted by page number.
     * This list is unmodifiable after construction.
     */
    private final List<Page> pages;

    /**
     * The page number currently being viewed.
     */
    private int currentPageNumber;

    /**
     * The name of the book.
     */
    private String title;

    /**
     * Constructs a new Book and initializes to the first page.
     *
     * @param pageList the list of pages to include in the book
     * @param bookTitle the title of the book
     */
    public Book(final String bookTitle, final List<Page> pageList) {
        if (bookTitle == null || bookTitle.isEmpty()) {
            throw new IllegalArgumentException(
                    "Book title cannot be null or empty.");
        }
        if (pageList == null || pageList.isEmpty()) {
            throw new IllegalArgumentException(
                    "A book must contain at least one page.");
        }

        this.title = bookTitle;

        // Make a defensive copy that we can sort
        java.util.List<Page> copy = new java.util.ArrayList<>(pageList);
        copy.sort(java.util.Comparator.comparingInt(Page::getPageNumber));

        this.pages = java.util.Collections.unmodifiableList(copy);
        this.currentPageNumber = copy.get(0).getPageNumber();
    }
    /**
     * @return the total number of pages in this book
     */
    public int getTotalPages() {
        return pages.size();
    }

    /**
     * @return the current page number being viewed
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * @return the current Page object being viewed
     */
    public Page getCurrentPage() {
        return getPage(currentPageNumber);
    }

    /**
     * Retrieves a page by its number.
     *
     * @param pageNumber the target page number
     * @return the corresponding Page
     */
    public Page getPage(final int pageNumber) {
        return pages.stream()
                .filter(page -> page.getPageNumber() == pageNumber)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Page number not found: " + pageNumber));
    }

    /**
     * Navigates to the specified page number.
     *
     * @param pageNumber the target page number
     */
    public void goToPage(final int pageNumber) {
        if (pages.stream().noneMatch(page ->
                page.getPageNumber() == pageNumber)) {
            throw new NoSuchElementException(
                    "Invalid page number: " + pageNumber);
        }
        this.currentPageNumber = pageNumber;
    }

    /**
     * Navigates to the next page if it exists.
     */
    public void nextPage() {
        final int next = currentPageNumber + 1;
        if (pages.stream().anyMatch(page -> page.getPageNumber() == next)) {
            currentPageNumber = next;
        }
    }

    /**
     * Navigates to the previous page if it exists.
     */
    public void previousPage() {
        final int prev = currentPageNumber - 1;
        if (pages.stream().anyMatch(page -> page.getPageNumber() == prev)) {
            currentPageNumber = prev;
        }
    }

    /**
     * @return the content string of the current page
     */
    public String getCurrentContent() {
        return getCurrentPage().getContent();
    }

    /**
     * @return an unmodifiable list of all pages
     */
    public List<Page> getAllPages() {
        return pages;
    }

    /**
     * @return the name of the book
     */
    public String getTitle() {
        return this.title;
    }
}
