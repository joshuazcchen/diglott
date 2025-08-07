package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single page in a book,
 * containing both original and translated scripts.
 */
public class Page {
    /**
     * The page number associated with this page.
     */
    private final int pageNumber;

    /**
     * The maximum number of words allowed on this page.
     */
    private final int maxWords;

    /**
     * The list of original words as they appeared in the source text.
     */
    private final List<String> originalWords;

    /**
     * The list of words currently displayed on
     * this page (translated or original).
     */
    private List<String> translatedWords;

    /**
     * If this page is already translated.
     */
    private boolean translated;

    /**
     * Constructs a Page object with given content and metadata.
     *
     * @param words      the original list of words for this page
     * @param pageNum the page number
     * @param max   the maximum number of words allowed
     */
    public Page(final List<String> words, final int pageNum, final int max) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException(
                    "Content cannot be null or empty.");
        }
        if (max <= 0) {
            throw new IllegalArgumentException("Max words must be positive.");
        }
        if (words.size() > max) {
            throw new IllegalArgumentException(
                    "Content exceeds the maximum allowed words per page: "
                            + words.size() + " > " + max);
        }

        this.pageNumber = pageNum;
        this.maxWords = max;
        this.translated = false;
        this.originalWords = new ArrayList<>(words);
        this.translatedWords = new ArrayList<>(words);
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
        return translated;
    }

    /**
     * Marks the page as translated.
     */
    public void translated() {
        this.translated = true;
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
