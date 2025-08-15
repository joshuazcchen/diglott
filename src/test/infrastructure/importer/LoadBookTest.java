package infrastructure.importer;

import configuration.ConfigDataRetriever;
import domain.model.Book;
import domain.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoadBook (.dig JSON importer).
 */
class LoadBookTest {

    @BeforeEach
    void setUp() {
        // Ensure maxWords is non-zero: page_length * page_length
        ConfigDataRetriever.set("page_length", "10");
        ConfigDataRetriever.saveConfig();
    }

    @Test
    void importBook_ReadsPagesAndSetsTranslated(@TempDir final Path dir)
            throws Exception {
        final String json = """
            [
              {"pageNumber": 2, "content": "two two", "translated": false},
              {"pageNumber": 1, "content": "one", "translated": true}
            ]
            """;

        final Path file = dir.resolve("sample.dig");
        Files.writeString(file, json, StandardCharsets.UTF_8);

        final Book book = LoadBook.importBook(file.toFile());

        assertEquals("sample.dig", book.getTitle());
        assertEquals(2, book.getTotalPages());

        // Book should be sorted internally: page 1 then 2
        final List<Page> pages = book.getAllPages();
        assertEquals(1, pages.get(0).getPageNumber());
        assertEquals(2, pages.get(1).getPageNumber());

        // Page 1 was marked translated
        final Page p1 = pages.get(0);
        assertTrue(p1.isTranslated());
        assertEquals(List.of("one"), p1.getWords());

        // Page 2 not translated
        final Page p2 = pages.get(1);
        assertFalse(p2.isTranslated());
        assertEquals(List.of("two", "two"), p2.getWords());
    }
}
