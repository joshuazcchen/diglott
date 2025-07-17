package Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {
    private final int pageNumber;
    private final int maxWords;
    private List<String> content;

    public Page(List<String> content, int pageNumber, int maxWords) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty.");
        }

        if (maxWords <= 0) {
            throw new IllegalArgumentException("Max words must be positive.");
        }

        if (content.size() > maxWords) {
            throw new IllegalArgumentException(
                    "Content exceeds the maximum allowed words per page: " + content.size() + " > " + maxWords
            );
        }

        this.pageNumber = pageNumber;
        this.maxWords = maxWords;
        this.content = new ArrayList<>(content);  // defensive copy
    }

    public String getContent() {
        return String.join(" ", this.content);
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(content);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public int getWordCount() {
        return content.size();
    }

    public void rewriteContent(List<String> words) {
        if (words == null || words.size() > maxWords) {
            throw new IllegalArgumentException("New content exceeds max words or is null.");
        }
        this.content = new ArrayList<>(words);  // defensive copy
    }
}
