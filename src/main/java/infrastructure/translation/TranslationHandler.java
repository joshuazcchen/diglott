package infrastructure.translation;

import Configuration.ConfigDataRetriever;
import infrastructure.persistence.StoredWords;
import domain.gateway.Translator;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles translation requests using the DeepL API and stores translated words.
 */
public class TranslationHandler implements Translator {

    private final String apiKey;
    private final StoredWords storedWords;

    /**
     * Creates a TranslationHandler instance.
     *
     * @param apiKey      the API key for the translation service, or {@code null} to use the config value
     * @param storedWords the storage for translated words
     */
    public TranslationHandler(final String apiKey, final StoredWords storedWords) {
        this.storedWords = storedWords;
        if (apiKey == null || apiKey.trim().isEmpty() || "none".equals(apiKey)) {
            this.apiKey = ConfigDataRetriever.get("api_key");
        } else {
            this.apiKey = apiKey;
            ConfigDataRetriever.set("api_key", apiKey);
            ConfigDataRetriever.saveConfig();
        }
    }

    @Override
    public void addWord(final String word) throws Exception {
        if ("none".equals(apiKey)) {
            throw new Exception("Missing API Key");
        }

        final String url = "https://api-free.deepl.com/v2/translate";
        final String urlParams = "auth_key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8)
                + "&text=" + URLEncoder.encode(word, StandardCharsets.UTF_8)
                + "&target_lang=" + ConfigDataRetriever.get("target_language");

        final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(urlParams.getBytes(StandardCharsets.UTF_8));
        }

        final StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
        }

        final String response = responseBuilder.toString();

        if ("debug".equals(ConfigDataRetriever.get("logs"))) {
            System.out.println("DeepL response: " + response);
            System.out.println(word);
        }

        final JSONObject responseJson = new JSONObject(response);
        final String translated = responseJson.getJSONArray("translations")
                .getJSONObject(0)
                .getString("text");

        if (!storedWords.getTranslations().containsKey(word.toLowerCase())) {
            storedWords.addTranslation(word.toLowerCase(), translated);
        }
    }
}
