package Book;

import java.util.ArrayList;
import java.util.List;

public class BookBuilder {
    private static final int WORDS_PER_PAGE = 10;

    public static Book createBookFromContent(String content) {
        String[] words = content.split("\\s+");
        List<String> pages = new ArrayList<>();
        StringBuilder pageContent = new StringBuilder();

        int wordCount = 0;
        for (String word : words) {
            pageContent.append(word).append(" ");
            wordCount++;
            if (wordCount >= WORDS_PER_PAGE) {
                pages.add(pageContent.toString().trim());
                pageContent.setLength(0);
                wordCount = 0;
            }
        }

        if (pageContent.length() > 0) {
            pages.add(pageContent.toString().trim());
        }

        Book book = new Book();
        for (int i = 0; i < pages.size(); i++) {
            book.addPage(new Page(i + 1, pages.get(i)));
        }

        return book;
    }
}
