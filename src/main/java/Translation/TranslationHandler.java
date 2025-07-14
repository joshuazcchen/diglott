package Translation;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import Configuration.ConfigDataRetriever;

public class TranslationHandler {
    private final String apiKey = ConfigDataRetriever.get("api_key");

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TranslationHandler th = new TranslationHandler();
        th.addWord("test");
    }
}

