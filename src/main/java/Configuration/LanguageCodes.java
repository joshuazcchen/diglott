package Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores bidirectional mappings between human-readable language names and their ISO language codes.
 */
public class LanguageCodes {

    /**
     * Maps language names to their ISO codes.
     */
    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>() {{
        put("Arabic", "ar");
        put("Bulgarian", "bg");
        put("Czech", "cs");
        put("Danish", "da");
        put("German", "de");
        put("Greek", "el");
        put("English (Britain)", "en-gb");
        put("English (American)", "en-us");
        put("Spanish", "es");
        put("Estonian", "et");
        put("Finnish", "fi");
        put("French", "fr");
        put("Hungarian", "hu");
        put("Indonesian", "id");
        put("Italian", "it");
        put("Japanese", "ja");
        put("Korean", "ko");
        put("Lithuanian", "lt");
        put("Latvian", "lv");
        put("Norwegian (Bokmål)", "nb");
        put("Dutch", "nl");
        put("Polish", "pl");
        put("Portuguese (Brazilian)", "pt-br");
        put("Portuguese (European)", "pt-pt");
        put("Romanian", "ro");
        put("Russian", "ru");
        put("Slovak", "sk");
        put("Slovenian", "sl");
        put("Swedish", "sv");
        put("Turkish", "tr");
        put("Ukrainian", "uk");
        put("Chinese", "zh");
        put("Chinese (simplified)", "zh-hans");
        put("Chinese (traditional)", "zh-hant");
    }};

    /**
     * Maps ISO codes to their language names.
     */
    public static final Map<String, String> REVERSELANGUAGES = new LinkedHashMap<>() {{
        put("ar", "Arabic");
        put("bg", "Bulgarian");
        put("cs", "Czech");
        put("da", "Danish");
        put("de", "German");
        put("el", "Greek");
        put("en-gb", "English (Britain)");
        put("en-us", "English (American)");
        put("es", "Spanish");
        put("et", "Estonian");
        put("fi", "Finnish");
        put("fr", "French");
        put("hu", "Hungarian");
        put("id", "Indonesian");
        put("it", "Italian");
        put("ja", "Japanese");
        put("ko", "Korean");
        put("lt", "Lithuanian");
        put("lv", "Latvian");
        put("nb", "Norwegian (Bokmål)");
        put("nl", "Dutch");
        put("pl", "Polish");
        put("pt-br", "Portuguese (Brazilian)");
        put("pt-pt", "Portuguese (European)");
        put("ro", "Romanian");
        put("ru", "Russian");
        put("sk", "Slovak");
        put("sl", "Slovenian");
        put("sv", "Swedish");
        put("tr", "Turkish");
        put("uk", "Ukrainian");
        put("zh", "Chinese");
        put("zh-hans", "Chinese (simplified)");
        put("zh-hant", "Chinese (traditional)");
    }};

    /**
     * Private constructor to prevent instantiation.
     */
    private LanguageCodes() {
        // Prevent instantiation
    }
}
