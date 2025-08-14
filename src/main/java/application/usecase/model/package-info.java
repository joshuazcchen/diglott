/**
 * Request and response models for application use cases.
 *
 * <p>This package contains immutable data transfer objects that cross the
 * use case boundary. These models carry data only and contain no business
 * logic, keeping the UI and controllers decoupled from domain entities and
 * infrastructure.</p>
 *
 * <p>Key models:</p>
 * <ul>
 *   <li>{@link application.usecase.model.ImportBookRequest} — input for the
 *       ImportBook use case, including the selected file and pagination
 *       size.</li>
 *   <li>{@link application.usecase.model.ImportBookResponse} — output from
 *       the ImportBook use case, including the created
 *       {@link domain.model.Book}, the extracted raw text, and the source
 *       {@link java.io.File}.</li>
 * </ul>
 */
package application.usecase.model;
