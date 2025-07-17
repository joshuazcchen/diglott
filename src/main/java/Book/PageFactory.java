package Book;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class PageFactory {

    public static List<Page> paginate(List<String> words, int maxWordsPerPage) {
        if (maxWordsPerPage <= 0) {
            throw new IllegalArgumentException("maxWordsPerPage must be greater than zero.");
        }

        List<Page> pages = new ArrayList<>();
        int pageNum = 1;

        for (int i = 0; i < words.size(); i += maxWordsPerPage) {
            int end = Math.min(i + maxWordsPerPage, words.size());

            StringJoiner joiner = new StringJoiner(" ");
            for (int j = i; j < end; j++) {
                joiner.add(words.get(j));
            }

            String pageContent = joiner.toString();
            pages.add(new Page(pageContent, pageNum++, maxWordsPerPage));
        }

        return pages;
    }
}


// TODO: Test pages like ts
// import Book.Book;
// import Book.Page;
// import Book.PageFactory;
//
// import java.util.Arrays;
// import java.util.List;

// String text = "This is a simple test book with multiple pages to demonstrate how pagination works in Java.";
// List<String> words = Arrays.asList(text.split("\\s+"));

// List<Page> pages = PageFactory.paginate(words, 5);
// Book book = new Book(pages);

// System.out.println("Total pages: " + book.getTotalPages());
//
// for (Page page : book.getAllPages()) {
//  System.out.println("Page " + page.getPageNumber() + ": " + page.getContent());
//  }
//
// System.out.println("\nTesting navigation:");
// System.out.println("Current page: " + book.getCurrentPageNumber());
//
// book.nextPage();
// System.out.println("Next page: " + book.getCurrentPageNumber());
// System.out.println(book.getCurrentPage().getContent());
//
// book.previousPage();
// System.out.println("Back to page: " + book.getCurrentPageNumber());
// System.out.println(book.getCurrentPage().getContent());

// book.goToPage(999); // Test the exception for invalid page LMAO