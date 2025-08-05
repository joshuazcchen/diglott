package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single page in a book, containing a limited number of words.
 */
public class Page {
    private final int pageNumber;
    private final int maxWords;
    private final List<String> originalWords;
    private List<String> content;
    private boolean translated;

    /**
     * Constructs a Page object with given content and metadata.
     *
     * @param words      the list of words for this page
     * @param pageNumber the page number
     * @param maxWords   the maximum number of words allowed
     */
    public Page(final List<String> words, final int pageNumber, final int maxWords) {
        this.translated = false;
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty.");
        }
        if (maxWords <= 0) {
            throw new IllegalArgumentException("Max words must be positive.");
        }
        if (words.size() > maxWords) {
            throw new IllegalArgumentException(
                    "Content exceeds the maximum allowed words per page: "
                            + words.size() + " > " + maxWords);
        }
        this.pageNumber = pageNumber;
        this.maxWords = maxWords;
        this.content = new ArrayList<>(words);
    }

    /**
     * @return the content as a single space-separated string
     */
    public String getContent() {
        return String.join(" ", this.content);
    }

    /**
     * @return the list of words on this page
     */
    public List<String> getWords() {
        return Collections.unmodifiableList(content);
    }

    /**
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @return true if the page has been translated
     */
    public boolean isTranslated() {
        return translated;
    }

    /**
     * Marks this page as translated.
     */
    public void translated() {
        translated = true;
    }

    /**
     * Replaces the content of the page with a new list of words.
     *
     * @param words the new list of words to set as content
     */
    public void rewriteContent(final List<String> words) {
        if (words == null || words.size() > maxWords) {
            throw new IllegalArgumentException(
                    "New content exceeds max words or is null.");
        }
        this.content = new ArrayList<>(words);
    }
}
