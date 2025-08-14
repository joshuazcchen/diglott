// application/usecase/ImportBookUseCase.java
package application.usecase;

import application.usecase.model.ImportBookRequest;
import application.usecase.model.ImportBookResponse;

import java.io.IOException;

/**
 * Use case boundary for importing a user-selected book file.
 * <p>
 * Implementations should depend only on domain and gateway interfaces
 * (DIP) and must not reference UI or infrastructure details.
 * </p>
 */
public interface ImportBookUseCase {

    /**
     * Imports the file described by the request and builds a {@code Book}.
     *
     * @param request input data containing the source file and pagination
     *                limit
     * @return a response holding the created {@code Book}, extracted raw
     *         text, and the source file
     * @throws IOException if the file cannot be read or parsed
     */
    ImportBookResponse importBook(ImportBookRequest request)
            throws IOException;
}
