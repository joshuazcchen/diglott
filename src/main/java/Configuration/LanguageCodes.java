package Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

public class LanguageCodes {
    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>() {{
        // TODO: also have the other way around such that we can maintain teh user selection
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
}
