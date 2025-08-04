package domain.model;

import java.util.ArrayList;
import java.util.List;

public class PageFactory {

    public static List<Page> paginate(List<String> words, int maxWordsPerPage) {
        if (maxWordsPerPage <= 0) {
            throw new IllegalArgumentException("maxWordsPerPage must be greater than zero.");
        }

        List<Page> pages = new ArrayList<>();
        int pageNum = 1;

        for (int i = 0; i < words.size(); i += maxWordsPerPage) {
            int end = Math.min(i + maxWordsPerPage, words.size());

            List<String> chunk = new ArrayList<>(words.subList(i, end));
            pages.add(new Page(chunk, pageNum++, maxWordsPerPage));
        }

        return pages;
    }
}
