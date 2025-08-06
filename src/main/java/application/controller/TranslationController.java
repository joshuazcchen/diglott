package application.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import configuration.ConfigDataRetriever;
import domain.gateway.BookImporter;
import domain.model.Page;
import domain.model.PageFactory;
import infrastructure.importer.BookImporterFactory;
import ui.components.FileSelector;

/**
 * Controller for loading and preparing book content for translation.
 */
public final class TranslationController {

    /**
     * Loads a book file, parses it into pages, and returns a result object.
     *
     * @return a LoadResult if successful, otherwise null
     */
    public LoadResult loadBook() {
        LoadResult result = null;
        final File selected = FileSelector.selectBookFile();

        if (selected != null) {
            try {
                final BookImporter importer =
                        BookImporterFactory.getImporter(selected);
                final String rawText = importer.importBook(selected);
                final List<String> words =
                        Arrays.asList(rawText.split(" "));
                final int pageLength =
                        ConfigDataRetriever.getInt("page_length");
                final List<Page> parsedPages =
                        PageFactory.paginate(words, pageLength);

                result = new LoadResult(parsedPages, rawText, selected);
            } catch (IOException ex) {
                System.err.println("Book loading failed: " + ex.getMessage());
                JOptionPane.showMessageDialog(
                        null, "Failed to load book."
                );
            }
        }

        return result;
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
         * Constructs a load result object containing loaded data.
         *
         * @param loadedPages the list of pages from the book
         * @param rawText     the raw text content of the book
         * @param original    the original file
         */
        public LoadResult(
                final List<Page> loadedPages,
                final String rawText,
                final File original
        ) {
            this.pages = loadedPages;
            this.text = rawText;
            this.file = original;
        }

        /**
         * Gets the list of parsed pages.
         *
         * @return the page list
         */
        public List<Page> getPages() {
            return pages;
        }

        /**
         * Gets the raw text of the book.
         *
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * Gets the original file object.
         *
         * @return the file
         */
        public File getFile() {
            return file;
        }
    }
}
