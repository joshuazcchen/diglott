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
public class TranslationController {

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
                final BookImporter importer = BookImporterFactory.getImporter(selected);
                final String text = importer.importBook(selected);
                final List<String> words = Arrays.asList(text.split(" "));
                final int pageLength = ConfigDataRetriever.getInt("page_length");
                final List<Page> pages = PageFactory.paginate(words, pageLength);
                result = new LoadResult(pages, text, selected);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load book.");
            }
        }
        return result;
    }

    /**
     * Container for book loading result.
     */
    public static class LoadResult {
        private final List<Page> pages;
        private final String text;
        private final File file;

        /**
         * Constructs a load result object containing loaded data.
         *
         * @param pages the list of pages generated from the book
         * @param text  the raw text content of the book
         * @param file  the original book file
         */
        public LoadResult(
                final List<Page> pages,
                final String text,
                final File file
        ) {
            this.pages = pages;
            this.text = text;
            this.file = file;
        }

        /**
         * Gets the list of pages.
         *
         * @return the list of pages
         */
        public List<Page> getPages() {
            return pages;
        }

        /**
         * Gets the raw text content.
         *
         * @return the text content
         */
        public String getText() {
            return text;
        }

        /**
         * Gets the original file.
         *
         * @return the file
         */
        public File getFile() {
            return file;
        }
    }
}
