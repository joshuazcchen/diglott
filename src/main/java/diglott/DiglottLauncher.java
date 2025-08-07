package diglott;

import ui.login.DeepLLoginUI;
import ui.main.MainUI;
import configuration.ConfigDataRetriever;

/**
 * Launches the Diglott application by opening the appropriate UI.
 */
public class DiglottLauncher {

    /**
     * Starts the application by attempting to load a saved API key.
     * If the key is valid,
     * it proceeds to the main UI; otherwise, it shows the login UI.
     */
    public void start() {
        String savedKey = null;
        try {
            savedKey = ConfigDataRetriever.get("deepl_api_key");
        } catch (Exception ignored) {
            // intentionally ignored
        }

        if (isValidDeepLApiKey(savedKey)) {
            MainUI.createInstance(savedKey).setVisible(true);
        } else {
            new DeepLLoginUI().setVisible(true);
        }
    }

    /**
     * Validates the given DeepL API key.
     *
     * @param key the key to validate
     * @return true if the key is non-null, non-empty, and not equal to "none"
     */
    private boolean isValidDeepLApiKey(final String key) {
        return key != null && !key.trim().isEmpty() && !"none".equals(key);
    }
}
