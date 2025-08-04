package application.controller;

import Configuration.ConfigDataRetriever;
import UI.components.FileSelector;
import domain.gateway.BookImporter;
import domain.model.Page;
import domain.model.PageFactory;
import infrastructure.importer.BookImporterFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TranslationController {

    public static class LoadResult {
        public final List<Page> pages;
        public final String text;
        public final File file;

        public LoadResult(List<Page> pages, String text, File file) {
            this.pages = pages;
            this.text = text;
            this.file = file;
        }
    }

    public LoadResult loadBook() {
        File selected = FileSelector.selectBookFile();
        if (selected != null) {
            try {
                BookImporter importer = BookImporterFactory.getImporter(selected);
                String text = importer.importBook(selected);
                List<String> words = Arrays.asList(text.split(" "));
                List<Page> pages = PageFactory.paginate(words, ConfigDataRetriever.getInt("page_length"));
                return new LoadResult(pages, text, selected);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load book.");
            }
        }
        return null;
    }
}
