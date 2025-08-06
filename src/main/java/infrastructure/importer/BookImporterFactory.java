package infrastructure.importer;

import domain.gateway.BookImporter;

import java.io.File;

/**
 * Factory class to return the correct BookImporter implementation
 * based on the file extension.
 */
public final class BookImporterFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private BookImporterFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns the appropriate BookImporter for the given file type.
     *
     * @param file the file to be imported
     * @return a BookImporter suitable for the file format
     * @throws IllegalArgumentException if the file extension is unsupported
     */
    public static BookImporter getImporter(final File file) {
        final String name = file.getName().toLowerCase();

        if (name.endsWith(".txt")) {
            return new TxtBookImporter();
        } else if (name.endsWith(".pdf")) {
            return new PdfBookImporter();
        } else if (name.endsWith(".epub")) {
            return new EpubBookImporter();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file format: " + name);
        }
    }
}
