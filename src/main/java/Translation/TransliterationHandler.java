package Translation;

import com.ibm.icu.text.Transliterator;

public class TransliterationHandler {

    private final Transliterator transliterator;

    public TransliterationHandler() {
        this.transliterator = Transliterator.getInstance("Any-Latin; Latin-ASCII");
    }

    public String transliterate(String input) {
        if (input == null) return "";
        return transliterator.transliterate(input);
    }
}
