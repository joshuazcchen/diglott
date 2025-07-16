package Translation;

import Configuration.ConfigDataRetriever;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TranslationHandler {
    private final String apiKey = ConfigDataRetriever.get("api_key");

    public String translateText(String text) throws Exception {
        if (apiKey.equals("none")) throw new Exception("Missing API Key");

        String url = "https://api-free.deepl.com/v2/translate";
        String urlParams = "auth_key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8) +
                "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target_lang=" + ConfigDataRetriever.get("target_language");

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(urlParams.getBytes());
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return in.lines().reduce("", (a, b) -> a + b);
        }
    }
}
