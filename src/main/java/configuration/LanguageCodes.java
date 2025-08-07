package configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores bidirectional mappings between
 * human-readable language names and their ISO language codes.
 */
public final class LanguageCodes {

    /**
     * Maps language names to their ISO codes.
     */
    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>() {{
        put("(DeepL) Arabic", "ar");
        put("(DeepL) Bulgarian", "bg");
        put("(DeepL) Czech", "cs");
        put("(DeepL) Danish", "da");
        put("(DeepL) German", "de");
        put("(DeepL) Greek", "el");
        put("(DeepL) English (Britain)", "en-gb");
        put("(DeepL) English (American)", "en-us");
        put("(DeepL) Spanish", "es");
        put("(DeepL) Estonian", "et");
        put("(DeepL) Finnish", "fi");
        put("(DeepL) French", "fr");
        put("(DeepL) Hungarian", "hu");
        put("(DeepL) Indonesian", "id");
        put("(DeepL) Italian", "it");
        put("(DeepL) Japanese", "ja");
        put("(DeepL) Korean", "ko");
        put("(DeepL) Lithuanian", "lt");
        put("(DeepL) Latvian", "lv");
        put("(DeepL) Norwegian (Bokmål)", "nb");
        put("(DeepL) Dutch", "nl");
        put("(DeepL) Polish", "pl");
        put("(DeepL) Portuguese (Brazilian)", "pt-br");
        put("(DeepL) Portuguese (European)", "pt-pt");
        put("(DeepL) Romanian", "ro");
        put("(DeepL) Russian", "ru");
        put("(DeepL) Slovak", "sk");
        put("(DeepL) Slovenian", "sl");
        put("(DeepL) Swedish", "sv");
        put("(DeepL) Turkish", "tr");
        put("(DeepL) Ukrainian", "uk");
        put("(DeepL) Chinese", "zh");
        put("(DeepL) Chinese (simplified)", "zh-hans");
        put("(DeepL) Chinese (traditional)", "zh-hant");

        /**
         * Maps bonus Azure language names to their ISO codes if the user has
         * a valid Azure API and region.
         */
        if ((!(ConfigDataRetriever.get("azure_api_key").equals("none")))
               && (!(ConfigDataRetriever.get("azure_region").equals("none")))) {
            put("(Azure) Afrikaans", "af");
            put("(Azure) Amharic", "am");
            put("(Azure) Assamese", "as");
            put("(Azure) Azerbaijani", "az");
            put("(Azure) Bashkir", "ba");
            put("(Azure) Belarusian", "be");
            put("(Azure) Bhojpuri", "bho");
            put("(Azure) Bangla", "bn");
            put("(Azure) Tibetan", "bo");
            put("(Azure) Bodo", "brx");
            put("(Azure) Bosnian", "bs");
            put("(Azure) Catalan", "ca");
            put("(Azure) Welsh", "cy");
            put("(Azure) Dogri", "doi");
            put("(Azure) Lower Sorbian", "dsb");
            put("(Azure) Divehi", "dv");
            put("(Azure) Basque", "eu");
            put("(Azure) Persian", "fa");
            put("(Azure) Filipino", "fil");
            put("(Azure) Fijian", "fj");
            put("(Azure) Faroese", "fo");
            put("(Azure) Quebecois", "fr-CA");
            put("(Azure) Irish", "ga");
            put("(Azure) Galician", "gl");
            put("(Azure) Konkani", "gom");
            put("(Azure) Gujarati", "gu");
            put("(Azure) Hausa", "ha");
            put("(Azure) Hebrew", "he");
            put("(Azure) Hindi", "hi");
            put("(Azure) Chhattisgarhi", "hne");
            put("(Azure) Croatian", "hr");
            put("(Azure) Upper Sorbian", "hsb");
            put("(Azure) Haitian Creole", "ht");
            put("(Azure) Armenian", "hy");
            put("(Azure) Igbo", "ig");
            put("(Azure) Inuinnaqtun", "ikt");
            put("(Azure) Icelandic", "is");
            put("(Azure) Inuktitut", "iu");
            put("(Azure) Georgian", "ka");
            put("(Azure) Kazakh", "kk");
            put("(Azure) Khmer", "km");
            put("(Azure) Kurmanji Kurdish", "kmr");
            put("(Azure) Kannada", "kn");
            put("(Azure) Kashmiri", "ks");
            put("(Azure) Sorani Kurdish", "ku");
            put("(Azure) Kyrgyz", "ky");
            put("(Azure) Luxembourgish", "lb");
            put("(Azure) Lingala", "ln");
            put("(Azure) Lao", "lo");
            put("(Azure) Ganda", "lug");
            put("(Azure) Literary Chinese", "lzh");
            put("(Azure) Maithili", "mai");
            put("(Azure) Malagasy", "mg");
            put("(Azure) Māori", "mi");
            put("(Azure) Macedonian", "mk");
            put("(Azure) Malayalam", "ml");
            put("(Azure) Mongolian (Cyrillic)", "mn-Cyrl");
            put("(Azure) Mongolian (Traditional)", "mn-Mong");
            put("(Azure) Manipuri", "mni");
            put("(Azure) Marathi", "mr");
            put("(Azure) Malay", "ms");
            put("(Azure) Maltese", "mt");
            put("(Azure) HmongDaw", "mww");
            put("(Azure) Myanmar/Burmese", "my");
            put("(Azure) Nepali", "ne");
            put("(Azure) Sesothosa Leboa", "nso");
            put("(Azure) Nyanja", "nya");
            put("(Azure) Odia", "or");
            put("(Azure) Querétaro Otomi", "otq");
            put("(Azure) Punjabi", "pa");
            put("(Azure) Dari", "prs");
            put("(Azure) Pashto", "ps");
            put("(Azure) Rundi", "run");
            put("(Azure) Kinyarwanda", "rw");
            put("(Azure) Sindhi", "sd");
            put("(Azure) Sinhala", "si");
            put("(Azure) Samoan", "sm");
            put("(Azure) Shona", "sn");
            put("(Azure) Somali", "so");
            put("(Azure) Albanian", "sq");
            put("(Azure) Serbian", "sr-Cyrl");
            put("(Azure) Sesotho", "st");
            put("(Azure) Swahili", "sw");
            put("(Azure) Tamil", "ta");
            put("(Azure) Telugu", "te");
            put("(Azure) Thai", "th");
            put("(Azure) Tigrinya", "ti");
            put("(Azure) Turkmen", "tk");
            put("(Azure) Klingon", "tlh-Latn");
            put("(Azure) Setswana", "tn");
            put("(Azure) Tongan", "to");
            put("(Azure) Tatar", "tt");
            put("(Azure) Tahitian", "ty");
            put("(Azure) Uyghur", "ug");
            put("(Azure) Urdu", "ur");
            put("(Azure) Uzbek", "uz");
            put("(Azure) Vietnamese", "vi");
            put("(Azure) Xhosa", "xh");
            put("(Azure) Yoruba", "yo");
            put("(Azure) Yucatec Maya", "yua");
            put("(Azure) Cantonese", "yue");
            put("(Azure) Zulu", "zu");
        }
    }};

    /**
     * Maps ISO codes to their language names.
     */
    public static final Map<String, String> REVERSELANGUAGES =
            new LinkedHashMap<>() {{
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

        /**
         * Maps bonus Azure language names to their ISO codes if the user has
         * a valid Azure API and region.
         */
        if ((!(ConfigDataRetriever.get("azure_api_key").equals("none")))
                && (!(ConfigDataRetriever.get("azure_region").equals("none")))) {
            put("af", "(Azure) Afrikaans");
            put("am", "(Azure) Amharic");
            put("as", "(Azure) Assamese");
            put("az", "(Azure) Azerbaijani");
            put("ba", "(Azure) Bashkir");
            put("be", "(Azure) Belarusian");
            put("bho", "(Azure) Bhojpuri");
            put("bn", "(Azure) Bangla");
            put("bo", "(Azure) Tibetan");
            put("brx", "(Azure) Bodo");
            put("bs", "(Azure) Bosnian");
            put("ca", "(Azure) Catalan");
            put("cy", "(Azure) Welsh");
            put("doi", "(Azure) Dogri");
            put("dsb", "(Azure) Lower Sorbian");
            put("dv", "(Azure) Divehi");
            put("eu", "(Azure) Basque");
            put("fa", "(Azure) Persian");
            put("fil", "(Azure) Filipino");
            put("fj", "(Azure) Fijian");
            put("fo", "(Azure) Faroese");
            put("fr-CA", "(Azure) Quebecois");
            put("ga", "(Azure) Irish");
            put("gl", "(Azure) Galician");
            put("gom", "(Azure) Konkani");
            put("gu", "(Azure) Gujarati");
            put("ha", "(Azure) Hausa");
            put("he", "(Azure) Hebrew");
            put("hi", "(Azure) Hindi");
            put("hne", "(Azure) Chhattisgarhi");
            put("hr", "(Azure) Croatian");
            put("hsb", "(Azure) Upper Sorbian");
            put("ht", "(Azure) Haitian Creole");
            put("hy", "(Azure) Armenian");
            put("ig", "(Azure) Igbo");
            put("ikt", "(Azure) Inuinnaqtun");
            put("is", "(Azure) Icelandic");
            put("iu", "(Azure) Inuktitut");
            put("ka", "(Azure) Georgian");
            put("kk", "(Azure) Kazakh");
            put("km", "(Azure) Khmer");
            put("kmr", "(Azure) Kurmanji Kurdish");
            put("kn", "(Azure) Kannada");
            put("ks", "(Azure) Kashmiri");
            put("ku", "(Azure) Sorani Kurdish");
            put("ky", "(Azure) Kyrgyz");
            put("lb", "(Azure) Luxembourgish");
            put("ln", "(Azure) Lingala");
            put("lo", "(Azure) Lao");
            put("lug", "(Azure) Ganda");
            put("lzh", "(Azure) Literary Chinese");
            put("mai", "(Azure) Maithili");
            put("mg", "(Azure) Malagasy");
            put("mi", "(Azure) Māori");
            put("mk", "(Azure) Macedonian");
            put("ml", "(Azure) Malayalam");
            put("mn-Cyrl", "(Azure) Mongolian (Cyrillic)");
            put("mn-Mong", "(Azure) Mongolian (Traditional)");
            put("mni", "(Azure) Manipuri");
            put("mr", "(Azure) Marathi");
            put("ms", "(Azure) Malay");
            put("mt", "(Azure) Maltese");
            put("mww", "(Azure) HmongDaw");
            put("my", "(Azure) Myanmar/Burmese");
            put("ne", "(Azure) Nepali");
            put("nso", "(Azure) Sesothosa Leboa");
            put("nya", "(Azure) Nyanja");
            put("or", "(Azure) Odia");
            put("otq", "(Azure) Querétaro Otomi");
            put("pa", "(Azure) Punjabi");
            put("prs", "(Azure) Dari");
            put("ps", "(Azure) Pashto");
            put("run", "(Azure) Rundi");
            put("rw", "(Azure) Kinyarwanda");
            put("sd", "(Azure) Sindhi");
            put("si", "(Azure) Sinhala");
            put("sm", "(Azure) Samoan");
            put("sn", "(Azure) Shona");
            put("so", "(Azure) Somali");
            put("sq", "(Azure) Albanian");
            put("sr-Cyrl", "(Azure) Serbian");
            put("st", "(Azure) Sesotho");
            put("sw", "(Azure) Swahili");
            put("ta", "(Azure) Tamil");
            put("te", "(Azure) Telugu");
            put("th", "(Azure) Thai");
            put("ti", "(Azure) Tigrinya");
            put("tk", "(Azure) Turkmen");
            put("tlh-Latn", "(Azure) Klingon");
            put("tn", "(Azure) Setswana");
            put("to", "(Azure) Tongan");
            put("tt", "(Azure) Tatar");
            put("ty", "(Azure) Tahitian");
            put("ug", "(Azure) Uyghur");
            put("ur", "(Azure) Urdu");
            put("uz", "(Azure) Uzbek");
            put("vi", "(Azure) Vietnamese");
            put("xh", "(Azure) Xhosa");
            put("yo", "(Azure) Yoruba");
            put("yua", "(Azure) Yucatec Maya");
            put("yue", "(Azure) Cantonese");
            put("zu", "(Azure) Zulu");
        }
    }};

    /**
     * Private constructor to prevent instantiation.
     */
    private LanguageCodes() {
        // Prevent instantiation
    }
}
