package application.interactor;

import configuration.ConfigDataRetriever;
import application.usecase.TranslatePageUseCase;
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import domain.model.Page;
import infrastructure.persistence.StoredWords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Interactor for executing the translation and transliteration of a page.
 * Applies the diglot method incrementally, using cached translations
 * and formatting logic.
 */
public final class TranslatePageInteractor implements TranslatePageUseCase {

    /** Handles word translation using an external service. */
    private final Translator translator;

    /** Converts translated words into a readable phonetic form. */
    private final WordTransliterator wordTransliterator;

    /** Stores and retrieves previously translated words. */
    private final StoredWords storedWords;

    /** Random number generator for selecting words to translate. */
    private final Random random;

    /** Minimum word length for eligibility to translate. */
    private static final int MIN_TRANSLATABLE_LENGTH = 3;

    /**
     * Constructs a TranslatePageInteractor with its required dependencies.
     *
     * @param translate         the translator used to retrieve translations
     * @param transliterator the transliterator used for formatting
     * @param stored       the word store for caching translations
     */
    public TranslatePageInteractor(
            final Translator translate,
            final WordTransliterator transliterator,
            final StoredWords stored
    ) {
        this.translator = translate;
        this.wordTransliterator = transliterator;
        this.storedWords = stored;
        this.random = new Random("DIGLOTTLANGUAGE".hashCode());
    }

    /**
     * Translates the content of a page and updates it with
     * transliterated replacements based on speed and page number.
     *
     * @param page the page to process and rewrite
     */
    @Override
    public void execute(final Page page) {
        final Map<String, String> wordDatabase =
                storedWords.getTranslations();
        final List<String> pageContent = page.getWords();

        final boolean incremental = ConfigDataRetriever.getBool("increment");
        final int configuredSpeed = ConfigDataRetriever.getSpeed();
        final int pageNumber = page.getPageNumber();

        final int internalSpeed = incremental
                ? (int) Math.floor(
                (double) pageNumber / (6 - configuredSpeed))
                : configuredSpeed;

        try {
            if (pageNumber != 0) {
                addRandomWordsToDatabase(
                        pageContent, wordDatabase, internalSpeed);
            }
        } catch (Exception e) {
            System.out.println("Translation error: " + e.getMessage());
        }

        final List<String> newPageContent =
                buildTranslatedContent(pageContent, wordDatabase);
        page.rewriteTranslatedContent(newPageContent);
    }

    /**
     * Adds random words from the page to the translation database
     * if they are not already translated.
     *
     * @param pageContent  the words on the page
     * @param wordDatabase the translation database
     * @param count        number of words to attempt adding
     * @throws Exception if translation API fails
     */
    private void addRandomWordsToDatabase(
            final List<String> pageContent,
            final Map<String, String> wordDatabase,
            final int count
    ) throws Exception {
        for (int z = 0; z < count; z++) {
            final int randomIndex = random.nextInt(pageContent.size());
            final String word = pageContent.get(randomIndex).toLowerCase();

            if (!wordDatabase.containsKey(word) && word.length()
                    >= MIN_TRANSLATABLE_LENGTH) {
                translator.addWord(pageContent.get(randomIndex));
            } else {
                z--; // Retry until 'count' words are added
            }
        }
    }

    /**
     * Builds the final page content by replacing translated words
     * with formatted versions.
     *
     * @param pageContent  the original page content
     * @param wordDatabase the translation database
     * @return a list of translated and formatted words
     */
    private List<String> buildTranslatedContent(
            final List<String> pageContent,
            final Map<String, String> wordDatabase
    ) {
        final List<String> newPageContent = new ArrayList<>();
        final boolean showOriginal =
                ConfigDataRetriever.getBool("original_script");

        for (String word : pageContent) {
            final String lower = word.toLowerCase();
            if (wordDatabase.containsKey(lower)) {
                final String translated = wordDatabase.get(lower);
                final String transliterated =
                        wordTransliterator.transliterate(translated);
                final String display = showOriginal
                        ? transliterated + "(" + translated + ")"
                        : transliterated;
                newPageContent.add("<b><u>" + display + "</u></b>");
            } else {
                newPageContent.add(word);
            }
        }

        return newPageContent;
    }
}
