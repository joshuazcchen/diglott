package infrastructure.importer;

import domain.gateway.BookImporter;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

public class EpubBookImporter implements BookImporter {

    @Override
    public String importBook(File file) throws IOException {
        try {
            Tika tika = new Tika();
            return tika.parseToString(file);
        } catch (TikaException e) {
            throw new IOException("Failed to parse EPUB: " + e.getMessage(), e);
        }
    }

}
