package application.interactor;

import application.usecase.ImportBookUseCase;
import application.usecase.model.ImportBookRequest;
import application.usecase.model.ImportBookResponse;
import domain.gateway.BookImporter;
import domain.gateway.ImporterSelector;
import domain.model.Book;
import domain.model.Page;
import domain.model.PageFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Imports a user file via a selected importer and builds a Book.
 * Depends only on boundaries and domain (DIP).
 */
public final class ImportBookInteractor implements ImportBookUseCase {

    /**
     * Boundary used to select the appropriate
     * {@link domain.gateway.BookImporter} for a given file.
     * Injected to keep the interactor independent of concrete
     * importers (DIP) and easy to unit test with fakes/stubs.
     */
    private final ImporterSelector selector;


    /**
     * @param importerSelector selector returning a proper BookImporter
     */
    public ImportBookInteractor(final ImporterSelector importerSelector) {
        this.selector = importerSelector;
    }

    @Override
    public ImportBookResponse importBook(final ImportBookRequest request)
            throws IOException {
        final BookImporter importer = selector.select(request.getFile());
        final String raw = importer.importBook(request.getFile());

        final List<String> words = Arrays.asList(raw.split("\\s+"));
        final List<Page> pages =
                PageFactory.paginate(words, request.getMaxWordsPerPage());

        final Book book = new Book(request.getFile().getName(), pages);

        // NOTE: now include raw text and file in the response
        return new ImportBookResponse(book, raw, request.getFile());
    }
}
