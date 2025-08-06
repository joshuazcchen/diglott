package domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class responsible for dividing a list of words into pages.
 */
public final class PageFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private PageFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Splits a list of words into pages with a
     * maximum number of words per page.
     *
     * @param words           the complete list of words to paginate
     * @param maxWordsPerPage the maximum number of words allowed per page
     * @return a list of Page objects representing paginated content
     * @throws IllegalArgumentException if maxWordsPerPage is not positive
     */
    public static List<Page> paginate(final List<String> words,
                                      final int maxWordsPerPage) {
        if (maxWordsPerPage <= 0) {
            throw new IllegalArgumentException(
                    "maxWordsPerPage must be greater than zero.");
        }

        List<Page> pages = new ArrayList<>();
        int pageNum = 1;

        for (int i = 0; i < words.size(); i += maxWordsPerPage) {
            int end = Math.min(i + maxWordsPerPage, words.size());
            List<String> chunk = new ArrayList<>(words.subList(i, end));
            pages.add(new Page(chunk, pageNum, maxWordsPerPage));
            pageNum++;
        }

        return pages;
    }
}
