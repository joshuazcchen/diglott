package infrastructure.importer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for EpubBookImporter.
 * We assert error propagation on unreadable input to avoid flaky EPUB crafting.
 */
class EpubBookImporterTest {

    @Test
    void importBook_ThrowsIOException_OnInvalidFile(@TempDir final Path dir) {
        final File notAnEpub = dir.resolve("notfound.epub").toFile();
        final EpubBookImporter importer = new EpubBookImporter();

        assertThrows(java.io.IOException.class,
                () -> importer.importBook(notAnEpub));
    }
}
