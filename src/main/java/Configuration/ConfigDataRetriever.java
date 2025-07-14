package Configuration;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigDataRetriever {

    private static JSONObject config;

    static {
        String content;
        try {
            String path = "java/Configuration/config.json";
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = new JSONObject(content);
    }

    public static String get(String key) {
        return config.getString(key);
    }

    public static int getSpeed() {
        return config.getInt("speed");
    }
}
