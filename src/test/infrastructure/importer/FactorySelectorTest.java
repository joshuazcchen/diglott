package infrastructure.importer;

import domain.gateway.BookImporter;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for FactorySelector adapter.
 */
class FactorySelectorTest {

    @Test
    void select_DelegatesToFactory() {
        final FactorySelector selector = new FactorySelector();

        final BookImporter txt = selector.select(new File("x.txt"));
        final BookImporter pdf = selector.select(new File("x.pdf"));
        final BookImporter epub = selector.select(new File("x.epub"));

        assertTrue(txt instanceof TxtBookImporter);
        assertTrue(pdf instanceof PdfBookImporter);
        assertTrue(epub instanceof EpubBookImporter);
    }
}
