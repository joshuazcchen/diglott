package Translation;

import Configuration.ConfigDataRetriever;

import java.util.HashMap;
import java.util.Map;

public class StoredWords {
    private Map<String, String> translated;

    public StoredWords() {
        translated = new HashMap<>();
    }

    public void addTranslation(String key, String value) {
        if (ConfigDataRetriever.get("logs").equals("debug")) {
            System.out.println(key + " " + value);
        }
        translated.put(key, value);
    }
    public Map<String, String> getTranslations() {
        return translated;
    }
}
