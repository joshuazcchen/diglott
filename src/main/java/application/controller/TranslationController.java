package application.controller;

import java.io.File;

import javax.swing.JOptionPane;

import application.usecase.ImportBookUseCase;
import application.usecase.model.ImportBookRequest;
import application.usecase.model.ImportBookResponse;
import domain.model.Book;
import domain.model.Page;
import ui.components.FileSelector;

import java.util.List;

/**
 * Controller for loading and preparing book content for translation.
 * <p>
 * Depends only on the ImportBook use case (DIP). Infra details such as
 * PDF/TXT/EPUB parsing are hidden behind the use case boundary.
 * </p>
 */
public final class TranslationController {
    /** Max words per page. */
    private final int pL = 100;

    /** Import book use case boundary. */
    private final ImportBookUseCase importBookUseCase;

    /**
     * Creates a controller that delegates to the import use case.
     *
     * @param importBook the ImportBook use case
     */
    public TranslationController(final ImportBookUseCase importBook) {
        this.importBookUseCase = importBook;
    }

    /**
     * Lets the user pick a file, imports it via the use case, and wraps
     * the result for the UI.
     *
     * @return a LoadResult if successful; otherwise null
     */
    public LoadResult loadBook() {
        final File selected = FileSelector.selectBookFile();
        if (selected == null) {
            return null;
        }

        try {
            final int maxPerPage = pL;

            final ImportBookRequest req =
                    new ImportBookRequest(selected, maxPerPage);

            final ImportBookResponse res =
                    importBookUseCase.importBook(req);

            final Book book = res.getBook();
            return new LoadResult(
                    book.getAllPages(),
                    res.getRawText(),
                    res.getFile()
            );
        } catch (Exception ex) {
            System.err.println("Book loading failed: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Failed to load book.");
            return null;
        }
    }

    /**
     * Container for book loading result.
     * Holds parsed pages, raw text, and the original file reference.
     */
    public static final class LoadResult {

        /** Parsed list of book pages. */
        private final List<Page> pages;

        /** Raw text content of the book. */
        private final String text;

        /** The original book file. */
        private final File file;

        /**
         * Creates a load result object containing loaded data.
         *
         * @param loadedPages the list of pages from the book
         * @param rawText     the raw text content of the book
         * @param original    the original file
         */
        public LoadResult(final List<Page> loadedPages,
                          final String rawText,
                          final File original) {
            this.pages = loadedPages;
            this.text = rawText;
            this.file = original;
        }

        /** @return the page list */
        public List<Page> getPages() {
            return pages;
        }

        /** @return the raw text */
        public String getText() {
            return text;
        }

        /** @return the original file */
        public File getFile() {
            return file;
        }
    }
}
