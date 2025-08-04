// AppLauncher.java
package Diglott;

import UI.login.LoginUI;
import UI.main.MainUI;
import Configuration.ConfigDataRetriever;

public class AppLauncher {

    public void start() {
        String savedKey = null;
        try {
            savedKey = ConfigDataRetriever.get("api_key");
        } catch (Exception ignored) {}

        if (isValidApiKey(savedKey)) {
            MainUI.createInstance(savedKey).setVisible(true);
        } else {
            new LoginUI().setVisible(true);
        }
    }

    private boolean isValidApiKey(String key) {
        return key != null && !key.trim().isEmpty() && !key.equals("none");
    }
}
