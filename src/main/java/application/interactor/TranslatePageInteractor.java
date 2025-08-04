// TranslatePageInteractor.java
package application.interactor;

import domain.model.Page;
import application.usecase.TranslatePageUseCase;
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import infrastructure.persistence.StoredWords;
import Configuration.ConfigDataRetriever;

import java.util.*;

public class TranslatePageInteractor implements TranslatePageUseCase {
    private final Translator translator;
    private final WordTransliterator wordTransliterator;
    private final StoredWords storedWords;
    private final Random random;

    public TranslatePageInteractor(Translator translator, WordTransliterator wordTransliterator, StoredWords storedWords) {
        this.translator = translator;
        this.wordTransliterator = wordTransliterator;
        this.storedWords = storedWords;
        this.random = new Random("DIGLOTTLANGUAGE".hashCode());
    }

    @Override
    public void execute(Page page) {
        Map<String, String> wordDatabase = storedWords.getTranslations();
        List<String> pageContent = page.getWords();
        page.translated();

        int internalSpeed = ConfigDataRetriever.getBool("increment")
                ? (int) Math.floor((double) page.getPageNumber() / ConfigDataRetriever.getSpeed())
                : ConfigDataRetriever.getSpeed();

        try {
            if (page.getPageNumber() != 0) {
                for (int z = 0; z < internalSpeed; z++) {
                    int randomIndex = random.nextInt(pageContent.size());
                    String word = pageContent.get(randomIndex).toLowerCase();
                    if (!wordDatabase.containsKey(word) && word.length() >= 3) {
                        translator.addWord(pageContent.get(randomIndex));
                    } else {
                        z--; // try again
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Translation error: " + e.getMessage());
        }

        List<String> newPageContent = new ArrayList<>();
        for (String word : pageContent) {
            String lower = word.toLowerCase();
            if (wordDatabase.containsKey(lower)) {
                String translated = wordDatabase.get(lower);
                String transliterated = wordTransliterator.transliterate(translated);
                String display = ConfigDataRetriever.getBool("original_script")
                        ? transliterated + "(" + translated + ")"
                        : transliterated;
                newPageContent.add("<b><u>" + display + "</u></b>");
            } else {
                newPageContent.add(word);
            }
        }

        page.rewriteContent(newPageContent);
    }
}
