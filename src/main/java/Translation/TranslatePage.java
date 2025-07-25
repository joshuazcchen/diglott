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
    private int internalSpeed;

    public TranslatePage(StoredWords storedWords) {
        this.storedWords = storedWords;
        this.random = new Random();
        random.setSeed("DIGLOTTLANGUAGE".hashCode());
        this.translationHandler = new TranslationHandler(ConfigDataRetriever.get("api_key"),
                storedWords);
    }

    public void translatePage(Page page) {
        Map<String, String> wordDatabase = storedWords.getTranslations();
        page.translated();
        List<String> pageContent = page.getWords();
        if (ConfigDataRetriever.getBool("increment")) {
            System.out.println("incrementing on");
            this.internalSpeed = (int) Math.floor((double) page.getPageNumber() /
                    ConfigDataRetriever.getSpeed());
        } else {
            System.out.println("incrementing off");
            this.internalSpeed = ConfigDataRetriever.getSpeed();
        }
        try {
            if (page.getPageNumber() != 0) {
                for (int z = 0; z < this.internalSpeed; z++) {
                    int randomIndex = random.nextInt(0, pageContent.size());
                    if (storedWords.getTranslations().containsKey(pageContent.get(randomIndex).
                            toLowerCase()) || pageContent.get(randomIndex).length() < 3) {
                        z--;
                    } else {
                        translationHandler.addWord(pageContent.get(randomIndex));
                    }
                }
            }
        } catch (Exception addWordException) {
            System.out.println("Error: " + addWordException.getMessage());;
        }
        List<String> newPageContent = new ArrayList<>();
        for (int i = 0; i < pageContent.size(); i++) {
            if (wordDatabase.containsKey(pageContent.get(i).toLowerCase())) {
                if (ConfigDataRetriever.get("logs").equals("debug")) {
                    System.out.println("added translated word to log:" + wordDatabase.get(pageContent.get(i).toLowerCase()));
                }
                newPageContent.add("<b>" + wordDatabase.get(pageContent.get(i).toLowerCase())
                        + "</b>");
            } else {
                newPageContent.add(pageContent.get(i));
            }
        }
        page.rewriteContent(newPageContent);
    }
}
