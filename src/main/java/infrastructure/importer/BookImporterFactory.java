package infrastructure.importer;

import domain.gateway.BookImporter;

import java.io.File;

public class BookImporterFactory {

    public static BookImporter getImporter(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".txt")) {
            return new TxtBookImporter();
        } else if (name.endsWith(".pdf")) {
            return new PdfBookImporter();
        } else if (name.endsWith(".epub")) {
            return new EpubBookImporter();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + name);
        }
    }
}
