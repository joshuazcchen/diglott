package infrastructure.exporter;

import configuration.ConfigDataRetriever;
import domain.model.Book;
import domain.model.Page;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;

public class SaveBook {
    /** The stored file path. */
    private static final Path PATH = Paths.get(
            System.getProperty("user.home"), ".diglott", "saves"
    );

    /** Prevents instantiation. */
    public SaveBook() {
    }

    /** Ensure the saves directory exists. */
    private void createSavesDirectory() {
        try {
            Files.createDirectories(PATH);
            System.out.println("Created directories: " + PATH);
            System.out.println("User home: " + System.getProperty("user.home"));
            System.out.println("Save directory: " + PATH.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save a book to appropriate file.
     * @param book is the book to save.
     * @return if success.
     */
    public boolean save(final Book book) {
        createSavesDirectory();
        Path folder = getSaveDirectory();

        String fileName = book.getTitle()
                + ConfigDataRetriever.get("target_language") + ".dig";
        Path filePath = folder.resolve(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("[");
            writer.newLine();

            List<Page> pages = book.getAllPages();
            for (int i = 0; i < pages.size(); i++) {
                Page page = pages.get(i);

                writer.write("  {");
                writer.newLine();
                writer.write("    \"pageNumber\": "
                        + page.getPageNumber() + ",");
                writer.newLine();
                writer.write("    \"content\": \""
                        + escapeJson(page.getContent()) + "\",");
                writer.newLine();
                writer.write("    \"translated\": \""
                        + escapeJson(page.isTranslated() ? "true"
                        : "false") + "\"");
                writer.write("  }");

                if (i < pages.size() - 1) {
                    writer.write(",");
                }
                writer.newLine();
            }

            writer.write("]");
            writer.newLine();

            System.out.println("Saving to: " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save book: " + e.getMessage());
            return false;
        }
    }

    /**
     * path retriever.
     * @return the path */
    public static Path getSaveDirectory() {
        return PATH;
    }
}
