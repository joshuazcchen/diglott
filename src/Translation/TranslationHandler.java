package Translation;
import java.io.*;
import java.net.*;

public class TranslationHandler {
    private final String apiKey = "API Key here (temporary)";
    // TODO: MOVE THIS TO A SEPARATE TOKEN FILE

    public void addWord(String word) {
        String url = "https://api-free.deepl.com/v2/translate";

        try {
            String urlParams = "auth_key=" + URLEncoder.encode(apiKey, "UTF-8") +
                    "&text=" + URLEncoder.encode(word, "UTF-8") +
                    "&target_lang=ZH";

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

    public static void main(String[] args) {
        TranslationHandler th = new TranslationHandler();
        th.addWord("test");
    }
}

