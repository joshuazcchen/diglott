package application.usecase.model;

import domain.model.Book;
import java.io.File;

/**
 * Response data for the {@code ImportBook} use case.
 * <p>
 * Immutable carrier that returns the imported {@link Book} plus the
 * original raw text extracted from the file and the file reference
 * itself (useful for display and persistence).
 * </p>
 */
public final class ImportBookResponse {

    /** The imported book entity created from the file's contents. */
    private final Book book;

    /** The unprocessed text extracted from the source file. */
    private final String rawText;

    /** The original user-selected file that was imported. */
    private final File file;

    /**
     * Creates a response for a successfully imported book.
     *
     * @param ebook    the constructed {@link Book} entity
     * @param rawTexts the original raw text extracted from the file
     * @param efile    the original file that was imported
     */
    public ImportBookResponse(final Book ebook,
                              final String rawTexts,
                              final File efile) {
        this.book = ebook;
        this.rawText = rawTexts;
        this.file = efile;
    }

    /**
     * Returns the imported book entity.
     *
     * @return the {@link Book}
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the raw, unprocessed text extracted from the file.
     *
     * @return extracted raw text
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * Returns the original file that was imported.
     *
     * @return the source file
     */
    public File getFile() {
        return file;
    }
}
