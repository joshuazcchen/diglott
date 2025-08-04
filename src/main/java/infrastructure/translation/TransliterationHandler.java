package infrastructure.translation;

import com.ibm.icu.text.Transliterator;
import domain.gateway.WordTransliterator;

public class TransliterationHandler implements WordTransliterator {

    private final Transliterator transliterator;

    public TransliterationHandler() {
        this.transliterator = Transliterator.getInstance("Any-Latin; Latin-ASCII");
    }

    @Override
    public String transliterate(String input) {
        if (input == null) return "";
        return transliterator.transliterate(input);
    }
}
