package infrastructure.importer;

import domain.gateway.BookImporter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * TXT file importer using UTF-8 decoding.
 */
public class TxtBookImporter implements BookImporter {

    /**
     * Reads the content of a UTF-8 encoded TXT file.
     *
     * @param file the TXT file to import
     * @return the file content as a string
     * @throws IOException if an I/O error occurs
     */
    @Override
    public String importBook(final File file) throws IOException {
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }
}
