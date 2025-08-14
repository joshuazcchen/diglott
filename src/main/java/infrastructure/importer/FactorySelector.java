package infrastructure.importer;

import domain.gateway.BookImporter;
import domain.gateway.ImporterSelector;

import java.io.File;

/**
 * Adapter that delegates to {@link BookImporterFactory}.
 */
public final class FactorySelector implements ImporterSelector {
    @Override
    public BookImporter select(final File file) {
        return BookImporterFactory.getImporter(file);
    }
}
