/**
 * Contains interactor implementations for executing core use case logic
 * in the Diglott application.
 *
 * <p>This package includes:
 * <ul>
 *     <li>{@code SpeakWordsInteractor}
 *     — invokes speech output via a {@code Speaker} gateway</li>
 *     <li>{@code TranslatePageInteractor}
 *     — handles translating and formatting page content</li>
 * </ul>
 *
 * <p>Each interactor implements a corresponding interface from
 * {@code application.usecase} and communicates with domain-layer gateways.
 * These classes are designed for use by controllers or UI layers.
 */
package application.interactor;
