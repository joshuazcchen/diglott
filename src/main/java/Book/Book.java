package Book;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private final List<Page> pages;
    private int currentPageIndex;

    public Book() {
        this.pages = new ArrayList<>();
        this.currentPageIndex = 0;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public int getPageCount() {
        return pages.size();
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public Page getCurrentPage() {
        if (pages.isEmpty()) {
            throw new IllegalStateException("No pages in the book.");
        }
        return pages.get(currentPageIndex);
    }

    public void nextPage() {
        if (currentPageIndex < pages.size() - 1) {
            currentPageIndex++;
        }
    }

    public void previousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
        }
    }

    public String showCurrentPageContent() {
        Page current = getCurrentPage();
        String content = current.getContent();
        System.out.println(content);
        return content;
    }
}
