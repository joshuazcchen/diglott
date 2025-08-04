package infrastructure.importer;

import domain.gateway.BookImporter;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

/**
 * EPUB importer implementation using Apache Tika.
 */
public class EpubBookImporter implements BookImporter {

    /**
     * Parses an EPUB file and returns its text content.
     *
     * @param file the EPUB file to import
     * @return the extracted text content
     * @throws IOException if parsing fails or file I/O error occurs
     */
    @Override
    public String importBook(final File file) throws IOException {
        try {
            final Tika tika = new Tika();
            return tika.parseToString(file);
        } catch (TikaException e) {
            throw new IOException("Failed to parse EPUB: " + e.getMessage(), e);
        }
    }
}
