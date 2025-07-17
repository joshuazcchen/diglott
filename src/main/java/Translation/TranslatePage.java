package Translation;

import Book.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Configuration.ConfigDataRetriever;

public class TranslatePage {
    private StoredWords storedWords;
    private Random random;
    private TranslationHandler translationHandler;

    public TranslatePage(StoredWords storedWords, Page page) {
        this.storedWords = storedWords;
        Random random = new Random();
        random.setSeed(7040733);
        this.translationHandler = new TranslationHandler(ConfigDataRetriever.get("api_key"));
    }

    public void translatePage(Page page) {
        Map<String, String> wordDatabase = storedWords.getTranslations();
        List<String> pageContent = page.getWords();
        try {
            if (page.getPageNumber() != 0) {
                for (int z = 0; z < ConfigDataRetriever.getSpeed(); z++) {
                    int randomIndex = random.nextInt(pageContent.size());
                    translationHandler.addWord(pageContent.get(randomIndex));
                }
            }
        } catch (Exception bad) {
            System.out.println(bad.getMessage());
        }
        for (int i = 0; i < pageContent.size(); i++) {
            if (wordDatabase.containsKey(pageContent.get(i))) {
                pageContent.set(i, wordDatabase.get(pageContent.get(i)));
            }
        }
        System.out.println("Translated words: " + pageContent);
        page.rewriteContent(pageContent);
    }
}
