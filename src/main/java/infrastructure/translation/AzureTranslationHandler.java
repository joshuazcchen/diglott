package infrastructure.translation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import configuration.ConfigDataRetriever;
import domain.gateway.Translator;
import infrastructure.persistence.StoredWords;

/**
 * Handles translation requests using the Azure API
 * and stores translated words in local memory.
 */
public class AzureTranslationHandler implements Translator {

    /**
     * API key used to authenticate with Azure.
     */
    private final String azureApiKey;

    /**
     * Azure service region used for API requests.
     */
    private final String azureRegion;

    /**
     * Local store of translated words.
     */
    private final StoredWords storedWords;

    /**
     * Creates a TranslationHandler instance.
     *
     * @param inputAzureApiKey
     * the API key for the translation service, or
     * {@code null} to use the config value
     * @param inputAzureRegion
     * the Azure service region,
     * or {@code null to use the config value
     * @param wordStorage
     * the storage for translated words
     */
    public AzureTranslationHandler(final String inputAzureApiKey,
                                   final String inputAzureRegion,
                                   final StoredWords wordStorage) {
        this.storedWords = wordStorage;

        if (inputAzureApiKey == null || inputAzureApiKey.trim().isEmpty()
                || "none".equals(inputAzureApiKey) || inputAzureRegion == null
                || inputAzureRegion.trim().isEmpty()
                || "none".equals(inputAzureRegion)) {
            this.azureApiKey = ConfigDataRetriever.get("azure_api_key");
            this.azureRegion = ConfigDataRetriever.get("azure_region");
        } else {
            this.azureApiKey = inputAzureApiKey;
            this.azureRegion = inputAzureRegion;
            ConfigDataRetriever.set("azure_api_key", inputAzureApiKey);
            ConfigDataRetriever.set("azure_region", inputAzureRegion);
            ConfigDataRetriever.saveConfig();
        }
    }

    /**
     * Translates a word using the Azure API and stores the result
     * if not already present.
     *
     * @param word the word to translate
     * @throws Exception if the API key is missing or the request fails
     */
    @Override
    public void addWord(final String word) throws Exception {
        if ("none".equals(azureApiKey)) {
            throw new Exception("Missing API Key");
        }

        final String targetLang = ConfigDataRetriever.get("target_language");
        final JSONObject responseJson = makeApiCall(word, targetLang);

        final JSONArray translations =
                responseJson.getJSONArray("translations");
        if (translations != null && translations.length() > 0) {
            final String translated =
                    translations.getJSONObject(0).getString("text");

            final String key = word.toLowerCase();
            if (!storedWords.getTranslations().containsKey(key)) {
                storedWords.addTranslation(key, translated);
            }
        }
    }

    /**
     * Makes a POST request to the Azure translation API.
     *
     * @param word the word to translate
     * @param targetLang the language to translate into
     * @return the JSON response as a JSONObject
     * @throws Exception if the request fails
     */
    protected JSONObject makeApiCall(final String word, final String targetLang)
            throws Exception {
        final String url = "https://api.cognitive.microsofttranslator.com/"
                + "translate?api-version=3.0&to=" + targetLang;

        final HttpURLConnection conn =
                (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", azureApiKey);
        conn.setRequestProperty("Ocp-Apim-Subscription-Region", azureRegion);
        conn.setRequestProperty("Content-Type",
                "application/json; charset=UTF-8");

        String jsonPayload = "[{\"Text\": \"" + word + "\"}]";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
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

        JSONArray topArray = new JSONArray(responseBuilder.toString());
        return topArray.getJSONObject(0);
    }
}
