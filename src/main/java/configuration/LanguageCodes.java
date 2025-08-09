package configuration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores bidirectional mappings between
 * human-readable language names and their ISO language codes.
 * Populates DeepL, Azure, or both language sets depending on keys.
 */
public final class LanguageCodes {
    /** the language codes. */
    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>();
    /** the reversed language codes so we can reverse map. */
    public static final Map<String, String> REVERSELANGUAGES =
            new LinkedHashMap<>();

    /** Language codes supported by DeepL. */
    public static final Set<String> DEEPL_LANG_CODES;
    /** Language codes supported by Azure. */
    public static final Set<String> AZURE_LANG_CODES;

    static {
        boolean hasDeepLKey;
        boolean hasAzureKey;
        boolean hasAzureRegion;

        try {
            String deepLKey = ConfigDataRetriever.get("deepl_api_key");
            hasDeepLKey = deepLKey != null && !deepLKey.equals("none");

            String azureKey = ConfigDataRetriever.get("azure_api_key");
            String azureRegion = ConfigDataRetriever.get("azure_region");
            hasAzureKey = azureKey != null && !azureKey.equals("none");
            hasAzureRegion = azureRegion != null && !azureRegion.equals("none");
        } catch (Exception e) {
            // Treat as no keys if exception occurs
            hasDeepLKey = false;
            hasAzureKey = false;
            hasAzureRegion = false;
        }

        Set<String> deepLSet = new LinkedHashSet<>();
        Set<String> azureSet = new LinkedHashSet<>();

        // Populate DeepL languages if DeepL key is present
        if (hasDeepLKey) {
            LANGUAGES.put("(DeepL) Arabic", "ar");
            LANGUAGES.put("(DeepL) Bulgarian", "bg");
            LANGUAGES.put("(DeepL) Czech", "cs");
            LANGUAGES.put("(DeepL) Danish", "da");
            LANGUAGES.put("(DeepL) German", "de");
            LANGUAGES.put("(DeepL) Greek", "el");
            LANGUAGES.put("(DeepL) English (Britain)", "en-gb");
            LANGUAGES.put("(DeepL) English (American)", "en-us");
            LANGUAGES.put("(DeepL) Spanish", "es");
            LANGUAGES.put("(DeepL) Estonian", "et");
            LANGUAGES.put("(DeepL) Finnish", "fi");
            LANGUAGES.put("(DeepL) French", "fr");
            LANGUAGES.put("(DeepL) Hungarian", "hu");
            LANGUAGES.put("(DeepL) Indonesian", "id");
            LANGUAGES.put("(DeepL) Italian", "it");
            LANGUAGES.put("(DeepL) Japanese", "ja");
            LANGUAGES.put("(DeepL) Korean", "ko");
            LANGUAGES.put("(DeepL) Lithuanian", "lt");
            LANGUAGES.put("(DeepL) Latvian", "lv");
            LANGUAGES.put("(DeepL) Norwegian (Bokmål)", "nb");
            LANGUAGES.put("(DeepL) Dutch", "nl");
            LANGUAGES.put("(DeepL) Polish", "pl");
            LANGUAGES.put("(DeepL) Portuguese (Brazilian)", "pt-br");
            LANGUAGES.put("(DeepL) Portuguese (European)", "pt-pt");
            LANGUAGES.put("(DeepL) Romanian", "ro");
            LANGUAGES.put("(DeepL) Russian", "ru");
            LANGUAGES.put("(DeepL) Slovak", "sk");
            LANGUAGES.put("(DeepL) Slovenian", "sl");
            LANGUAGES.put("(DeepL) Swedish", "sv");
            LANGUAGES.put("(DeepL) Turkish", "tr");
            LANGUAGES.put("(DeepL) Ukrainian", "uk");
            LANGUAGES.put("(DeepL) Chinese", "zh");
            LANGUAGES.put("(DeepL) Chinese (simplified)", "zh-hans");
            LANGUAGES.put("(DeepL) Chinese (traditional)", "zh-hant");

            // Populate reverse map and DeepL set
            for (Map.Entry<String, String> entry : LANGUAGES.entrySet()) {
                if (entry.getKey().startsWith("(DeepL)")) {
                    REVERSELANGUAGES.put(entry.getValue(), entry.getKey());
                    deepLSet.add(entry.getValue());
                }
            }
        }

        // Populate Azure languages if Azure key & region are present
        if (hasAzureKey && hasAzureRegion) {
            LANGUAGES.put("(Azure) Afrikaans", "af");
            LANGUAGES.put("(Azure) Amharic", "am");
            LANGUAGES.put("(Azure) Assamese", "as");
            LANGUAGES.put("(Azure) Azerbaijani", "az");
            LANGUAGES.put("(Azure) Bashkir", "ba");
            LANGUAGES.put("(Azure) Belarusian", "be");
            LANGUAGES.put("(Azure) Bhojpuri", "bho");
            LANGUAGES.put("(Azure) Bangla", "bn");
            LANGUAGES.put("(Azure) Tibetan", "bo");
            LANGUAGES.put("(Azure) Bodo", "brx");
            LANGUAGES.put("(Azure) Bosnian", "bs");
            LANGUAGES.put("(Azure) Catalan", "ca");
            LANGUAGES.put("(Azure) Welsh", "cy");
            LANGUAGES.put("(Azure) Dogri", "doi");
            LANGUAGES.put("(Azure) Lower Sorbian", "dsb");
            LANGUAGES.put("(Azure) Divehi", "dv");
            LANGUAGES.put("(Azure) Basque", "eu");
            LANGUAGES.put("(Azure) Persian", "fa");
            LANGUAGES.put("(Azure) Filipino", "fil");
            LANGUAGES.put("(Azure) Fijian", "fj");
            LANGUAGES.put("(Azure) Faroese", "fo");
            LANGUAGES.put("(Azure) Quebecois", "fr-CA");
            LANGUAGES.put("(Azure) Irish", "ga");
            LANGUAGES.put("(Azure) Galician", "gl");
            LANGUAGES.put("(Azure) Konkani", "gom");
            LANGUAGES.put("(Azure) Gujarati", "gu");
            LANGUAGES.put("(Azure) Hausa", "ha");
            LANGUAGES.put("(Azure) Hebrew", "he");
            LANGUAGES.put("(Azure) Hindi", "hi");
            LANGUAGES.put("(Azure) Chhattisgarhi", "hne");
            LANGUAGES.put("(Azure) Croatian", "hr");
            LANGUAGES.put("(Azure) Upper Sorbian", "hsb");
            LANGUAGES.put("(Azure) Haitian Creole", "ht");
            LANGUAGES.put("(Azure) Armenian", "hy");
            LANGUAGES.put("(Azure) Igbo", "ig");
            LANGUAGES.put("(Azure) Inuinnaqtun", "ikt");
            LANGUAGES.put("(Azure) Icelandic", "is");
            LANGUAGES.put("(Azure) Inuktitut", "iu");
            LANGUAGES.put("(Azure) Georgian", "ka");
            LANGUAGES.put("(Azure) Kazakh", "kk");
            LANGUAGES.put("(Azure) Khmer", "km");
            LANGUAGES.put("(Azure) Kurmanji Kurdish", "kmr");
            LANGUAGES.put("(Azure) Kannada", "kn");
            LANGUAGES.put("(Azure) Kashmiri", "ks");
            LANGUAGES.put("(Azure) Sorani Kurdish", "ku");
            LANGUAGES.put("(Azure) Kyrgyz", "ky");
            LANGUAGES.put("(Azure) Luxembourgish", "lb");
            LANGUAGES.put("(Azure) Lingala", "ln");
            LANGUAGES.put("(Azure) Lao", "lo");
            LANGUAGES.put("(Azure) Ganda", "lug");
            LANGUAGES.put("(Azure) Literary Chinese", "lzh");
            LANGUAGES.put("(Azure) Maithili", "mai");
            LANGUAGES.put("(Azure) Malagasy", "mg");
            LANGUAGES.put("(Azure) Māori", "mi");
            LANGUAGES.put("(Azure) Macedonian", "mk");
            LANGUAGES.put("(Azure) Malayalam", "ml");
            LANGUAGES.put("(Azure) Mongolian (Cyrillic)", "mn-Cyrl");
            LANGUAGES.put("(Azure) Mongolian (Traditional)", "mn-Mong");
            LANGUAGES.put("(Azure) Manipuri", "mni");
            LANGUAGES.put("(Azure) Marathi", "mr");
            LANGUAGES.put("(Azure) Malay", "ms");
            LANGUAGES.put("(Azure) Maltese", "mt");
            LANGUAGES.put("(Azure) HmongDaw", "mww");
            LANGUAGES.put("(Azure) Myanmar/Burmese", "my");
            LANGUAGES.put("(Azure) Nepali", "ne");
            LANGUAGES.put("(Azure) Sesothosa Leboa", "nso");
            LANGUAGES.put("(Azure) Nyanja", "nya");
            LANGUAGES.put("(Azure) Odia", "or");
            LANGUAGES.put("(Azure) Querétaro Otomi", "otq");
            LANGUAGES.put("(Azure) Punjabi", "pa");
            LANGUAGES.put("(Azure) Dari", "prs");
            LANGUAGES.put("(Azure) Pashto", "ps");
            LANGUAGES.put("(Azure) Rundi", "run");
            LANGUAGES.put("(Azure) Kinyarwanda", "rw");
            LANGUAGES.put("(Azure) Sindhi", "sd");
            LANGUAGES.put("(Azure) Sinhala", "si");
            LANGUAGES.put("(Azure) Samoan", "sm");
            LANGUAGES.put("(Azure) Shona", "sn");
            LANGUAGES.put("(Azure) Somali", "so");
            LANGUAGES.put("(Azure) Albanian", "sq");
            LANGUAGES.put("(Azure) Serbian", "sr-Cyrl");
            LANGUAGES.put("(Azure) Sesotho", "st");
            LANGUAGES.put("(Azure) Swahili", "sw");
            LANGUAGES.put("(Azure) Tamil", "ta");
            LANGUAGES.put("(Azure) Telugu", "te");
            LANGUAGES.put("(Azure) Thai", "th");
            LANGUAGES.put("(Azure) Tigrinya", "ti");
            LANGUAGES.put("(Azure) Turkmen", "tk");
            LANGUAGES.put("(Azure) Klingon", "tlh-Latn");
            LANGUAGES.put("(Azure) Setswana", "tn");
            LANGUAGES.put("(Azure) Tongan", "to");
            LANGUAGES.put("(Azure) Tatar", "tt");
            LANGUAGES.put("(Azure) Tahitian", "ty");
            LANGUAGES.put("(Azure) Uyghur", "ug");
            LANGUAGES.put("(Azure) Urdu", "ur");
            LANGUAGES.put("(Azure) Uzbek", "uz");
            LANGUAGES.put("(Azure) Vietnamese", "vi");
            LANGUAGES.put("(Azure) Xhosa", "xh");
            LANGUAGES.put("(Azure) Yoruba", "yo");
            LANGUAGES.put("(Azure) Yucatec Maya", "yua");
            LANGUAGES.put("(Azure) Cantonese", "yue");
            LANGUAGES.put("(Azure) Zulu", "zu");

            // Populate reverse map and Azure set
            for (Map.Entry<String, String> entry : LANGUAGES.entrySet()) {
                if (entry.getKey().startsWith("(Azure)")) {
                    REVERSELANGUAGES.put(entry.getValue(), entry.getKey());
                    azureSet.add(entry.getValue());
                }
            }
        }

        DEEPL_LANG_CODES = Collections.unmodifiableSet(deepLSet);
        AZURE_LANG_CODES = Collections.unmodifiableSet(azureSet);
    }

    private LanguageCodes() {
        // Prevent instantiation
    }
}
