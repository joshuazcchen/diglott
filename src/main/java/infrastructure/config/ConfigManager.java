package infrastructure.config;

import domain.gateway.ConfigGateway;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class ConfigManager implements ConfigGateway {

    private final String configPath = "src/main/java/Configuration/config.json";

    @Override
    public String getGoogleCredentialsPath() {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(configPath));
            JSONObject jsonObject = (JSONObject) obj;
            return (String) jsonObject.get("credentialsPath");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
