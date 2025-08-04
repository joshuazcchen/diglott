package infrastructure.importer;

import domain.gateway.BookImporter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * PDF importer implementation using Apache PDFBox.
 */
public class PdfBookImporter implements BookImporter {

    /**
     * Parses a PDF file and extracts its text content.
     *
     * @param file the PDF file to import
     * @return the extracted text content
     * @throws IOException if an error occurs during parsing
     */
    @Override
    public String importBook(final File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            final PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
