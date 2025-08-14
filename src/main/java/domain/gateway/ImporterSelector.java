package domain.gateway;

import java.io.File;

/**
 * Chooses a {@link BookImporter} implementation for a given file.
 */
public interface ImporterSelector {
    /**
     * @param file user-selected book file
     * @return a concrete BookImporter suitable for the file
     */
    BookImporter select(File file);
}
