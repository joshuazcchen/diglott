/**
 * Provides gateway interfaces that abstract external dependencies.
 * <p>
 * These interfaces define contracts for interacting with external systems and services
 * such as text-to-speech engines, translation APIs, file parsers, and transliteration tools.
 * </p>
 *
 * <p>Key interfaces include:</p>
 * <ul>
 *   <li>{@link domain.gateway.Speaker} — for speaking words using text-to-speech.</li>
 *   <li>{@link domain.gateway.BookImporter} — for importing book content from various file formats.</li>
 *   <li>{@link domain.gateway.Translator} — for translating words and storing results.</li>
 *   <li>{@link domain.gateway.WordTransliterator} — for converting text between scripts.</li>
 * </ul>
 *
 * <p>
 * Following the Dependency Inversion Principle (DIP), these gateway interfaces
 * allow the application core to remain decoupled from specific external implementations,
 * promoting testability and modularity.
 * </p>
 */
package domain.gateway;
