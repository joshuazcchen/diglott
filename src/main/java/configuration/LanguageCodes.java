package configuration;

import java.util.LinkedHashMap;
import java.util.Map;

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

    static {
        boolean hasDeepLKey = false;
        boolean hasAzureKey = false;
        boolean hasAzureRegion = false;

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

            REVERSELANGUAGES.put("ar", "(DeepL) Arabic");
            REVERSELANGUAGES.put("bg", "(DeepL) Bulgarian");
            REVERSELANGUAGES.put("cs", "(DeepL) Czech");
            REVERSELANGUAGES.put("da", "(DeepL) Danish");
            REVERSELANGUAGES.put("de", "(DeepL) German");
            REVERSELANGUAGES.put("el", "(DeepL) Greek");
            REVERSELANGUAGES.put("en-gb", "(DeepL) English (Britain)");
            REVERSELANGUAGES.put("en-us", "(DeepL) English (American)");
            REVERSELANGUAGES.put("es", "(DeepL) Spanish");
            REVERSELANGUAGES.put("et", "(DeepL) Estonian");
            REVERSELANGUAGES.put("fi", "(DeepL) Finnish");
            REVERSELANGUAGES.put("fr", "(DeepL) French");
            REVERSELANGUAGES.put("hu", "(DeepL) Hungarian");
            REVERSELANGUAGES.put("id", "(DeepL) Indonesian");
            REVERSELANGUAGES.put("it", "(DeepL) Italian");
            REVERSELANGUAGES.put("ja", "(DeepL) Japanese");
            REVERSELANGUAGES.put("ko", "(DeepL) Korean");
            REVERSELANGUAGES.put("lt", "(DeepL) Lithuanian");
            REVERSELANGUAGES.put("lv", "(DeepL) Latvian");
            REVERSELANGUAGES.put("nb", "(DeepL) Norwegian (Bokmål)");
            REVERSELANGUAGES.put("nl", "(DeepL) Dutch");
            REVERSELANGUAGES.put("pl", "(DeepL) Polish");
            REVERSELANGUAGES.put("pt-br", "(DeepL) Portuguese (Brazilian)");
            REVERSELANGUAGES.put("pt-pt", "(DeepL) Portuguese (European)");
            REVERSELANGUAGES.put("ro", "(DeepL) Romanian");
            REVERSELANGUAGES.put("ru", "(DeepL) Russian");
            REVERSELANGUAGES.put("sk", "(DeepL) Slovak");
            REVERSELANGUAGES.put("sl", "(DeepL) Slovenian");
            REVERSELANGUAGES.put("sv", "(DeepL) Swedish");
            REVERSELANGUAGES.put("tr", "(DeepL) Turkish");
            REVERSELANGUAGES.put("uk", "(DeepL) Ukrainian");
            REVERSELANGUAGES.put("zh", "(DeepL) Chinese");
            REVERSELANGUAGES.put("zh-hans", "(DeepL) Chinese (simplified)");
            REVERSELANGUAGES.put("zh-hant", "(DeepL) Chinese (traditional)");
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

            REVERSELANGUAGES.put("af", "(Azure) Afrikaans");
            REVERSELANGUAGES.put("am", "(Azure) Amharic");
            REVERSELANGUAGES.put("as", "(Azure) Assamese");
            REVERSELANGUAGES.put("az", "(Azure) Azerbaijani");
            REVERSELANGUAGES.put("ba", "(Azure) Bashkir");
            REVERSELANGUAGES.put("be", "(Azure) Belarusian");
            REVERSELANGUAGES.put("bho", "(Azure) Bhojpuri");
            REVERSELANGUAGES.put("bn", "(Azure) Bangla");
            REVERSELANGUAGES.put("bo", "(Azure) Tibetan");
            REVERSELANGUAGES.put("brx", "(Azure) Bodo");
            REVERSELANGUAGES.put("bs", "(Azure) Bosnian");
            REVERSELANGUAGES.put("ca", "(Azure) Catalan");
            REVERSELANGUAGES.put("cy", "(Azure) Welsh");
            REVERSELANGUAGES.put("doi", "(Azure) Dogri");
            REVERSELANGUAGES.put("dsb", "(Azure) Lower Sorbian");
            REVERSELANGUAGES.put("dv", "(Azure) Divehi");
            REVERSELANGUAGES.put("eu", "(Azure) Basque");
            REVERSELANGUAGES.put("fa", "(Azure) Persian");
            REVERSELANGUAGES.put("fil", "(Azure) Filipino");
            REVERSELANGUAGES.put("fj", "(Azure) Fijian");
            REVERSELANGUAGES.put("fo", "(Azure) Faroese");
            REVERSELANGUAGES.put("fr-CA", "(Azure) Quebecois");
            REVERSELANGUAGES.put("ga", "(Azure) Irish");
            REVERSELANGUAGES.put("gl", "(Azure) Galician");
            REVERSELANGUAGES.put("gom", "(Azure) Konkani");
            REVERSELANGUAGES.put("gu", "(Azure) Gujarati");
            REVERSELANGUAGES.put("ha", "(Azure) Hausa");
            REVERSELANGUAGES.put("he", "(Azure) Hebrew");
            REVERSELANGUAGES.put("hi", "(Azure) Hindi");
            REVERSELANGUAGES.put("hne", "(Azure) Chhattisgarhi");
            REVERSELANGUAGES.put("hr", "(Azure) Croatian");
            REVERSELANGUAGES.put("hsb", "(Azure) Upper Sorbian");
            REVERSELANGUAGES.put("ht", "(Azure) Haitian Creole");
            REVERSELANGUAGES.put("hy", "(Azure) Armenian");
            REVERSELANGUAGES.put("ig", "(Azure) Igbo");
            REVERSELANGUAGES.put("ikt", "(Azure) Inuinnaqtun");
            REVERSELANGUAGES.put("is", "(Azure) Icelandic");
            REVERSELANGUAGES.put("iu", "(Azure) Inuktitut");
            REVERSELANGUAGES.put("ka", "(Azure) Georgian");
            REVERSELANGUAGES.put("kk", "(Azure) Kazakh");
            REVERSELANGUAGES.put("km", "(Azure) Khmer");
            REVERSELANGUAGES.put("kmr", "(Azure) Kurmanji Kurdish");
            REVERSELANGUAGES.put("kn", "(Azure) Kannada");
            REVERSELANGUAGES.put("ks", "(Azure) Kashmiri");
            REVERSELANGUAGES.put("ku", "(Azure) Sorani Kurdish");
            REVERSELANGUAGES.put("ky", "(Azure) Kyrgyz");
            REVERSELANGUAGES.put("lb", "(Azure) Luxembourgish");
            REVERSELANGUAGES.put("ln", "(Azure) Lingala");
            REVERSELANGUAGES.put("lo", "(Azure) Lao");
            REVERSELANGUAGES.put("lug", "(Azure) Ganda");
            REVERSELANGUAGES.put("lzh", "(Azure) Literary Chinese");
            REVERSELANGUAGES.put("mai", "(Azure) Maithili");
            REVERSELANGUAGES.put("mg", "(Azure) Malagasy");
            REVERSELANGUAGES.put("mi", "(Azure) Māori");
            REVERSELANGUAGES.put("mk", "(Azure) Macedonian");
            REVERSELANGUAGES.put("ml", "(Azure) Malayalam");
            REVERSELANGUAGES.put("mn-Cyrl", "(Azure) Mongolian (Cyrillic)");
            REVERSELANGUAGES.put("mn-Mong", "(Azure) Mongolian (Traditional)");
            REVERSELANGUAGES.put("mni", "(Azure) Manipuri");
            REVERSELANGUAGES.put("mr", "(Azure) Marathi");
            REVERSELANGUAGES.put("ms", "(Azure) Malay");
            REVERSELANGUAGES.put("mt", "(Azure) Maltese");
            REVERSELANGUAGES.put("mww", "(Azure) HmongDaw");
            REVERSELANGUAGES.put("my", "(Azure) Myanmar/Burmese");
            REVERSELANGUAGES.put("ne", "(Azure) Nepali");
            REVERSELANGUAGES.put("nso", "(Azure) Sesothosa Leboa");
            REVERSELANGUAGES.put("nya", "(Azure) Nyanja");
            REVERSELANGUAGES.put("or", "(Azure) Odia");
            REVERSELANGUAGES.put("otq", "(Azure) Querétaro Otomi");
            REVERSELANGUAGES.put("pa", "(Azure) Punjabi");
            REVERSELANGUAGES.put("prs", "(Azure) Dari");
            REVERSELANGUAGES.put("ps", "(Azure) Pashto");
            REVERSELANGUAGES.put("run", "(Azure) Rundi");
            REVERSELANGUAGES.put("rw", "(Azure) Kinyarwanda");
            REVERSELANGUAGES.put("sd", "(Azure) Sindhi");
            REVERSELANGUAGES.put("si", "(Azure) Sinhala");
            REVERSELANGUAGES.put("sm", "(Azure) Samoan");
            REVERSELANGUAGES.put("sn", "(Azure) Shona");
            REVERSELANGUAGES.put("so", "(Azure) Somali");
            REVERSELANGUAGES.put("sq", "(Azure) Albanian");
            REVERSELANGUAGES.put("sr-Cyrl", "(Azure) Serbian");
            REVERSELANGUAGES.put("st", "(Azure) Sesotho");
            REVERSELANGUAGES.put("sw", "(Azure) Swahili");
            REVERSELANGUAGES.put("ta", "(Azure) Tamil");
            REVERSELANGUAGES.put("te", "(Azure) Telugu");
            REVERSELANGUAGES.put("th", "(Azure) Thai");
            REVERSELANGUAGES.put("ti", "(Azure) Tigrinya");
            REVERSELANGUAGES.put("tk", "(Azure) Turkmen");
            REVERSELANGUAGES.put("tlh-Latn", "(Azure) Klingon");
            REVERSELANGUAGES.put("tn", "(Azure) Setswana");
            REVERSELANGUAGES.put("to", "(Azure) Tongan");
            REVERSELANGUAGES.put("tt", "(Azure) Tatar");
            REVERSELANGUAGES.put("ty", "(Azure) Tahitian");
            REVERSELANGUAGES.put("ug", "(Azure) Uyghur");
            REVERSELANGUAGES.put("ur", "(Azure) Urdu");
            REVERSELANGUAGES.put("uz", "(Azure) Uzbek");
            REVERSELANGUAGES.put("vi", "(Azure) Vietnamese");
            REVERSELANGUAGES.put("xh", "(Azure) Xhosa");
            REVERSELANGUAGES.put("yo", "(Azure) Yoruba");
            REVERSELANGUAGES.put("yua", "(Azure) Yucatec Maya");
            REVERSELANGUAGES.put("yue", "(Azure) Cantonese");
            REVERSELANGUAGES.put("zu", "(Azure) Zulu");
        }
    }

    private LanguageCodes() {
        // Prevent instantiation
    }
}
