/**.
 * Provides file import functionality for different book formats
 * in the Diglott application, including:
 * <ul>
 *     <li>{@code PdfBookImporter}
 *    — handles importing text from PDF files</li>
 *     <li>{@code TxtBookImporter}
 *    — handles importing plain text files</li>
 *     <li>{@code EpubBookImporter}
 *     — handles importing content from EPUB files</li>
 *     <li>{@code BookImporterFactory}
 *     — returns the correct importer instance
 *     based on the file extension</li>
 * </ul>
 */
package infrastructure.importer;
