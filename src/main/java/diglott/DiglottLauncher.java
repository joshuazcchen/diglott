package diglott;

import ui.login.LoginUI;
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
        String savedDeepL = "none";
        String savedAzure = "none";
        String savedAzureRegion = "none";

        try {
            savedDeepL = ConfigDataRetriever.get("deepl_api_key");
        } catch (Exception ignored) { }
        try {
            savedAzure = ConfigDataRetriever.get("azure_api_key");
        } catch (Exception ignored) { }
        try {
            savedAzureRegion = ConfigDataRetriever.get("azure_region");
        } catch (Exception ignored) { }
        boolean hasDeepL = isValidApiKey(savedDeepL);
        boolean hasAzure = isValidApiKey(savedAzure);
        boolean hasAzureRegion = isValidApiKey(savedAzureRegion);

        if (hasDeepL || (hasAzure && hasAzureRegion)) {
            MainUI.createInstance(
                    hasDeepL ? savedDeepL : "none",
                    hasAzure ? savedAzure : "none",
                    (hasAzure && hasAzureRegion) ? savedAzureRegion : "none"
            ).setVisible(true);
        } else {
            new LoginUI().setVisible(true);
        }
    }

    /**
     * Validates the given API key.
     *
     * @param key the key to validate
     * @return true if the key is non-null, non-empty, and not equal to "none"
     */
    private boolean isValidApiKey(final String key) {
        return key != null && !key.trim().isEmpty() && !"none".equals(key);
    }
}
