package infrastructure.importer;

import domain.gateway.BookImporter;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BookImporterFactoryTest {

    @Test
    void returnsTxtBookImporterForTxtFile() {
        File file = new File("example.txt");
        BookImporter importer = BookImporterFactory.getImporter(file);
        assertTrue(importer instanceof TxtBookImporter);
    }

    @Test
    void returnsPdfBookImporterForPdfFile() {
        File file = new File("example.PDF");
        BookImporter importer = BookImporterFactory.getImporter(file);
        assertTrue(importer instanceof PdfBookImporter);
    }

    @Test
    void returnsEpubBookImporterForEpubFile() {
        File file = new File("example.epub");
        BookImporter importer = BookImporterFactory.getImporter(file);
        assertTrue(importer instanceof EpubBookImporter);
    }

    @Test
    void throwsExceptionForUnsupportedExtension() {
        File file = new File("invalid.docx");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                BookImporterFactory.getImporter(file)
        );
        assertTrue(exception.getMessage().contains("Unsupported file format"));
    }
}
