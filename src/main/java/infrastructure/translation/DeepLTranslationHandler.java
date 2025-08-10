package infrastructure.translation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import configuration.ConfigDataRetriever;
import domain.gateway.Translator;
import infrastructure.persistence.StoredWords;

/**
 * Handles translation requests using the DeepL API
 * and stores translated words in local memory.
 */
public class DeepLTranslationHandler implements Translator {

    /**
     * API key used to authenticate with DeepL.
     */
    private final String deepLApiKey;

    /**
     * Local store of translated words.
     */
    private final StoredWords storedWords;

    /**
     * Creates a TranslationHandler instance.
     *
     * @param inputDeepLApiKey
     * the API key for the translation service, or
     * {@code null} to use the config value
     * @param wordStorage
     * the storage for translated words
     */
    public DeepLTranslationHandler(final String inputDeepLApiKey,
                                   final StoredWords wordStorage) {
        this.storedWords = wordStorage;

        if (inputDeepLApiKey == null || inputDeepLApiKey.trim().isEmpty()
                || "none".equals(inputDeepLApiKey)) {
            this.deepLApiKey = ConfigDataRetriever.get("deepl_api_key");
        } else {
            this.deepLApiKey = inputDeepLApiKey;
            ConfigDataRetriever.set("deepl_api_key", inputDeepLApiKey);
            ConfigDataRetriever.saveConfig();
        }
    }

    /**
     * Translates a word using the DeepL API and stores the result
     * if not already present.
     *
     * @param word the word to translate
     * @throws Exception if the API key is missing or the request fails
     */
    @Override
    public void addWord(final String word) throws Exception {
        if ("none".equals(deepLApiKey)) {
            throw new Exception("Missing API Key");
        }

        final String encodedKey = URLEncoder.encode(
                deepLApiKey, StandardCharsets.UTF_8);
        final String encodedWord = URLEncoder.encode(
                word, StandardCharsets.UTF_8);
        final String targetLang = ConfigDataRetriever.get("target_language");

        final String urlParams = "auth_key=" + encodedKey
                + "&text=" + encodedWord
                + "&target_lang=" + targetLang;

        final JSONObject responseJson = makeApiCall(urlParams);

        final JSONArray translations =
                responseJson.optJSONArray("translations");
        if (translations != null && !translations.isEmpty()) {
            final String translated =
                    translations.getJSONObject(0).getString("text");

            final String key = word.toLowerCase();
            if (!storedWords.getTranslations().containsKey(key)) {
                storedWords.addTranslation(key, translated);
            }
        }
    }

    /**
     * Makes a POST request to the DeepL translation API.
     *
     * @param urlParams the encoded request parameters
     * @return the JSON response as a JSONObject
     * @throws Exception if the request fails
     */
    protected JSONObject makeApiCall(final String urlParams)
            throws Exception {
        final String url = "https://api-free.deepl.com/v2/translate";

        final HttpURLConnection conn =
                (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(urlParams.getBytes(StandardCharsets.UTF_8));
        }

        final StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
        }

        return new JSONObject(responseBuilder.toString());
    }
}
