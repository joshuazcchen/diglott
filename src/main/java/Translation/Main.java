package Translation;

import UI.LoginUI;
import UI.MainUI;
import Configuration.ConfigDataRetriever;

public class Main {
    public static void main(String[] args) {
        String savedKey = null;
        try {
            savedKey = ConfigDataRetriever.get("api_key");
        } catch (Exception e) {
        }

        if (savedKey != null && !savedKey.trim().isEmpty() && !savedKey.equals("none")) {
            MainUI.createInstance(savedKey).setVisible(true);
        } else {
            new LoginUI().setVisible(true);
        }
    }
}
