package application.interactor;

import application.usecase.model.ImportBookRequest;
import application.usecase.model.ImportBookResponse;
import domain.gateway.BookImporter;
import domain.gateway.ImporterSelector;
import domain.model.Book;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Mockito-style tests for ImportBookInteractor. */
class ImportBookInteractorTest {

    @Test
    void importBuildsBook_choosesImporter_andPaginates() throws Exception {
        File file = new File("story.txt");
        String raw = "one two three four";

        // mocks
        ImporterSelector selector = mock(ImporterSelector.class);
        BookImporter importer = mock(BookImporter.class);

        when(selector.select(file)).thenReturn(importer);
        when(importer.importBook(file)).thenReturn(raw);

        ImportBookInteractor interactor = new ImportBookInteractor(selector);

        ImportBookResponse res =
                interactor.importBook(new ImportBookRequest(file, 2));

        // verify interactions
        verify(selector).select(file);
        verify(importer).importBook(file);

        // assertions
        Book book = res.getBook();
        assertNotNull(book);
        assertEquals(2, book.getAllPages().size()); // 4 words, 2/page
        assertEquals(raw, res.getRawText());
        assertEquals(file, res.getFile());
    }

    @Test
    void propagatesIOExceptionFromImporter() throws Exception {
        File file = new File("bad.pdf");

        ImporterSelector selector = mock(ImporterSelector.class);
        BookImporter importer = mock(BookImporter.class);

        when(selector.select(file)).thenReturn(importer);
        when(importer.importBook(file)).thenThrow(new IOException("boom"));

        ImportBookInteractor interactor = new ImportBookInteractor(selector);

        assertThrows(IOException.class, () ->
                interactor.importBook(new ImportBookRequest(file, 100)));

        verify(selector).select(file);
        verify(importer).importBook(file);
    }
}
