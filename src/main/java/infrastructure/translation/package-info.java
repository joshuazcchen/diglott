/**.
 * Provides infrastructure-level translation and transliteration services
 * for the Diglott application, including:
 * <ul>
 *     <li>{@code TransliterationHandler} —
 *     uses ICU4J to convert words from any script
 *     to Latin ASCII characters</li>
 *     <li>{@code DeepLTranslationHandler} —
 *     connects to the DeepL API to perform translations
 *     and stores translated words for reuse</li>
 *     <li>{@code AzureTranslationHandler} —
 *     connects to the Azure API to perform translations
 *     and stores translated words for reuse</li>
 *     <li>{@code PageTranslationTask} —
 *     wraps page translation in a background task
 *     using {@code SwingWorker} to keep the UI responsive</li>
 * </ul>
 */
package infrastructure.translation;
