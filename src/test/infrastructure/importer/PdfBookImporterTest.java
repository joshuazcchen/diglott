package infrastructure.importer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration-style test for PdfBookImporter using a tiny PDF created on the fly.
 */
class PdfBookImporterTest {

    @Test
    void importBook_ExtractsText(@TempDir final Path dir) throws Exception {
        final Path pdfPath = dir.resolve("tiny.pdf");

        try (PDDocument doc = new PDDocument()) {
            final PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(72, 700);
                cs.showText("Hello PDFBox");
                cs.endText();
            }

            doc.save(pdfPath.toFile());
        }

        final PdfBookImporter importer = new PdfBookImporter();
        final String text = importer.importBook(pdfPath.toFile());

        assertTrue(text.contains("Hello PDFBox"));
    }
}
