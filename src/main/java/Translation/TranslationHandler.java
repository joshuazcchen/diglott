package Translation;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import Configuration.ConfigDataRetriever;
import org.json.JSONObject;

public class TranslationHandler {
    private final String apiKey;
    private final StoredWords storedWords;

    public TranslationHandler(String apiKey) {
        this.storedWords = new StoredWords();
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("none")) {
            this.apiKey = ConfigDataRetriever.get("api_key");  // fallback to config
        } else {
            this.apiKey = apiKey;
        }
    }

    public void addWord(String word) throws Exception {
        String url = "https://api-free.deepl.com/v2/translate";
        if (apiKey.equals("none")) {
            throw new Exception("Missing API Key");
        }
        try {
            String urlParams = "auth_key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8) +
                    "&text=" + URLEncoder.encode(word, StandardCharsets.UTF_8) +
                    "&target_lang=" + ConfigDataRetriever.get("target_language");

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(urlParams.getBytes());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.lines().reduce("", (a, b) -> a + b);

            System.out.println("DeepL response: " + response);
            JSONObject responseJson = new JSONObject(response);
            String translated = responseJson.getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("text");
            storedWords.addTranslation(word, translated);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

}

