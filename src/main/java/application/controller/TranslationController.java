package application.controller;

import configuration.ConfigDataRetriever;
import UI.components.FileSelector;
import domain.gateway.BookImporter;
import domain.model.Page;
import domain.model.PageFactory;
import infrastructure.importer.BookImporterFactory;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for loading and preparing book content for translation.
 */
public class TranslationController {

    /**
     * Container for book loading result.
     */
    public static class LoadResult {
        public final List<Page> pages;
        public final String text;
        public final File file;

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
    }

    /**
     * Loads a book file, parses it into pages, and returns a result object.
     *
     * @return a LoadResult if successful, otherwise null
     */
    public LoadResult loadBook() {
        final File selected = FileSelector.selectBookFile();
        if (selected != null) {
            try {
                final BookImporter importer = BookImporterFactory.getImporter(selected);
                final String text = importer.importBook(selected);
                final List<String> words = Arrays.asList(text.split(" "));
                final int pageLength = ConfigDataRetriever.getInt("page_length");
                final List<Page> pages = PageFactory.paginate(words, pageLength);
                return new LoadResult(pages, text, selected);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load book.");
            }
        }
        return null;
    }
}
