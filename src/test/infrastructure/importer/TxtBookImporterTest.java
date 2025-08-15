package infrastructure.importer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for TxtBookImporter.
 */
class TxtBookImporterTest {

    @Test
    void importBook_ReadsUtf8Text(@TempDir final Path dir) throws Exception {
        final Path f = dir.resolve("sample.txt");
        Files.writeString(f, "hello world\nsecond line");

        final TxtBookImporter importer = new TxtBookImporter();
        final String content = importer.importBook(f.toFile());

        // Simple containment checks
        assertEquals(true, content.contains("hello world"));
        assertEquals(true, content.contains("second line"));
    }
}
