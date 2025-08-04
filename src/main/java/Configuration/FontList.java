package Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a predefined list of available fonts used in the Diglott UI.
 */
public final class FontList {

    /**
     * A map of font display names to their corresponding font names.
     * Preserves insertion order.
     */
    public static final Map<String, String> FONTS;

    static {
        FONTS = new LinkedHashMap<>();
        FONTS.put("Arial", "Arial");
        FONTS.put("Verdana", "Verdana");
        FONTS.put("Tahoma", "Tahoma");
        FONTS.put("Trebuchet MS", "Trebuchet MS");
        FONTS.put("Times New Roman", "Times New Roman");
        FONTS.put("Georgia", "Georgia");
        FONTS.put("Garamond", "Garamond");
        FONTS.put("Courier New", "Courier New");
        FONTS.put("Brush Script MT", "Brush Script MT");
    }

    private FontList() {
        // Prevent instantiation
    }
}
