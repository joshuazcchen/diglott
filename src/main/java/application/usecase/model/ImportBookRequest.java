package application.usecase.model;

import java.io.File;

/**
 * Request data for the {@code ImportBook} use case.
 * <p>
 * Immutable carrier of the user-selected file and the maximum words
 * allowed per page for pagination.
 * </p>
 */
public final class ImportBookRequest {

    /** The user-selected book file to import. */
    private final File file;

    /** Maximum number of words allowed on each page. */
    private final int maxWordsPerPage;

    /**
     * Creates a new request for importing a book.
     *
     * @param bookFile   the file selected by the user (must exist/readable)
     * @param maxPerPage the maximum number of words per page used
     *                   when paginating the imported text
     */
    public ImportBookRequest(final File bookFile, final int maxPerPage) {
        this.file = bookFile;
        this.maxWordsPerPage = maxPerPage;
    }

    /**
     * Returns the user-selected book file to import.
     *
     * @return the file to be imported
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the pagination limit used when creating pages.
     *
     * @return maximum number of words per page
     */
    public int getMaxWordsPerPage() {
        return maxWordsPerPage;
    }
}
