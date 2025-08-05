package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single page in a book, containing both original and translated scripts.
 */
public class Page {
    private final int pageNumber;
    private final int maxWords;
    private final List<String> originalWords;
    private List<String> translatedWords;

    /**
     * Constructs a Page object with given content and metadata.
     *
     * @param words      the original list of words for this page
     * @param pageNumber the page number
     * @param maxWords   the maximum number of words allowed
     */
    public Page(final List<String> words, final int pageNumber, final int maxWords) {
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
        this.originalWords = new ArrayList<>(words);
        this.translatedWords = new ArrayList<>(words); // initially same as original
    }

    /**
     * @return the translated content as a single space-separated string
     */
    public String getContent() {
        return String.join(" ", this.translatedWords);
    }

    /**
     * @return the original words (unmodifiable)
     */
    public List<String> getOriginalWords() {
        return Collections.unmodifiableList(originalWords);
    }

    /**
     * @return the translated words (unmodifiable)
     */
    public List<String> getWords() {
        return Collections.unmodifiableList(translatedWords);
    }

    /**
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @return true if the translated words differ from the original words
     */
    public boolean isTranslated() {
        return !Objects.equals(originalWords, translatedWords);
    }

    /**
     * Replaces the translated content of the page with a new list of words.
     *
     * @param words the new translated list of words
     */
    public void rewriteTranslatedContent(final List<String> words) {
        if (words == null || words.size() > maxWords) {
            throw new IllegalArgumentException(
                    "New content exceeds max words or is null.");
        }
        this.translatedWords = new ArrayList<>(words);
    }

    /**
     * Resets the translated words back to the original script.
     */
    public void resetToOriginal() {
        this.translatedWords = new ArrayList<>(originalWords);
    }
}
