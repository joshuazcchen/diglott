package application.interactor;

import Configuration.ConfigDataRetriever;
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
 */
public class TranslatePageInteractor implements TranslatePageUseCase {

    private final Translator translator;
    private final WordTransliterator wordTransliterator;
    private final StoredWords storedWords;
    private final Random random;

    /**
     * Constructs a TranslatePageInteractor with its required dependencies.
     *
     * @param translator         the translator used to retrieve translations
     * @param wordTransliterator the transliterator used for formatting
     * @param storedWords        the word store for caching translations
     */
    public TranslatePageInteractor(
            final Translator translator,
            final WordTransliterator wordTransliterator,
            final StoredWords storedWords
    ) {
        this.translator = translator;
        this.wordTransliterator = wordTransliterator;
        this.storedWords = storedWords;
        this.random = new Random("DIGLOTTLANGUAGE".hashCode());
    }

    /**
     * Translates the content of a page and updates it with formatted replacements.
     *
     * @param page the page to process and rewrite
     */
    @Override
    public void execute(final Page page) {
        final Map<String, String> wordDatabase = storedWords.getTranslations();
        final List<String> pageContent = page.getWords();

        final boolean incremental = ConfigDataRetriever.getBool("increment");
        final int configuredSpeed = ConfigDataRetriever.getSpeed();
        final int pageNumber = page.getPageNumber();

        final int internalSpeed = incremental
                ? (int) Math.floor((double) pageNumber / configuredSpeed)
                : configuredSpeed;

        try {
            if (pageNumber != 0) {
                addRandomWordsToDatabase(pageContent, wordDatabase, internalSpeed);
            }
        } catch (Exception e) {
            System.out.println("Translation error: " + e.getMessage());
        }

        final List<String> newPageContent = buildTranslatedContent(pageContent, wordDatabase);
        page.rewriteTranslatedContent(newPageContent);
    }

    /**
     * Adds random words from the page to the translation database if not already present.
     *
     * @param pageContent  the words on the page
     * @param wordDatabase the translation database
     * @param count        number of words to attempt adding
     */
    private void addRandomWordsToDatabase(
            final List<String> pageContent,
            final Map<String, String> wordDatabase,
            final int count
    ) throws Exception {
        for (int z = 0; z < count; z++) {
            final int randomIndex = random.nextInt(pageContent.size());
            final String word = pageContent.get(randomIndex).toLowerCase();
            if (!wordDatabase.containsKey(word) && word.length() >= 3) {
                translator.addWord(pageContent.get(randomIndex));
            } else {
                z--;
            }
        }
    }

    /**
     * Builds the translated page content.
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
        final boolean showOriginal = ConfigDataRetriever.getBool("original_script");

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
