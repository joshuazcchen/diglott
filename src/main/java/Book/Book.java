package Book;

import java.util.*;

public class Book {
    private final List<Page> pages;
    private int currentPageNumber;

    public Book(List<Page> pages) {
        if (pages == null || pages.isEmpty()) {
            throw new IllegalArgumentException("A book must contain at least one page.");
        }

        pages.sort(Comparator.comparingInt(Page::getPageNumber));
        this.pages = Collections.unmodifiableList(pages);
        // Start at first page
        this.currentPageNumber = pages.get(0).getPageNumber();
    }

    public int getTotalPages() {
        return pages.size();
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public Page getCurrentPage() {
        return getPage(currentPageNumber);
    }

    public Page getPage(int pageNumber) {
        return pages.stream()
                .filter(p -> p.getPageNumber() == pageNumber)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Page number not found: " + pageNumber));
    }

    public void goToPage(int pageNumber) {
        if (pages.stream().noneMatch(p -> p.getPageNumber() == pageNumber)) {
            throw new NoSuchElementException("Invalid page number: " + pageNumber);
        }
        this.currentPageNumber = pageNumber;
    }

    public void nextPage() {
        int next = currentPageNumber + 1;
        if (pages.stream().anyMatch(p -> p.getPageNumber() == next)) {
            currentPageNumber = next;
        }
    }

    public void previousPage() {
        int prev = currentPageNumber - 1;
        if (pages.stream().anyMatch(p -> p.getPageNumber() == prev)) {
            currentPageNumber = prev;
        }
    }

    public List<Page> getAllPages() {
        return pages;
    }
}
