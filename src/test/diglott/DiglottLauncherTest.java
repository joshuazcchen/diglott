package diglott;

import static org.mockito.Mockito.*;

import configuration.ConfigDataRetriever;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import ui.login.LoginUI;
import ui.main.MainUI;

/**
 * Tests for {@link DiglottLauncher} without MockitoExtension.
 */
class DiglottLauncherTest {

    /**
     * Creates a fresh launcher instance.
     *
     * @return a new {@link DiglottLauncher}
     */
    private DiglottLauncher newLauncher() {
        return new DiglottLauncher();
    }

    /**
     * Verifies that when a valid DeepL key exists, the launcher opens
     * MainUI with DeepL and "none" for Azure fields.
     */
    @Test
    @DisplayName("start(): shows MainUI when DeepL key is valid")
    void start_DeepLPresent_OpensMainUI() {
        try (MockedStatic<ConfigDataRetriever> cfg =
                     mockStatic(ConfigDataRetriever.class);
             MockedStatic<MainUI> mainUiStatic =
                     mockStatic(MainUI.class);
             MockedConstruction<LoginUI> loginCtor =
                     mockConstruction(LoginUI.class)) {

            cfg.when(() -> ConfigDataRetriever.get("deepl_api_key"))
                    .thenReturn("DEEPL_OK");
            cfg.when(() -> ConfigDataRetriever.get("azure_api_key"))
                    .thenReturn("none");
            cfg.when(() -> ConfigDataRetriever.get("azure_region"))
                    .thenReturn("none");

            final MainUI mainUi = mock(MainUI.class);
            mainUiStatic
                    .when(() -> MainUI.createInstance("DEEPL_OK",
                            "none", "none"))
                    .thenReturn(mainUi);

            newLauncher().start();

            verify(mainUi).setVisible(true);
            assert loginCtor.constructed().isEmpty();
        }
    }

    /**
     * Verifies that when Azure key and region exist (without DeepL),
     * the launcher opens MainUI with Azure and region populated.
     */
    @Test
    @DisplayName("start(): shows MainUI when Azure + region valid")
    void start_AzurePresent_OpensMainUI() {
        try (MockedStatic<ConfigDataRetriever> cfg =
                     mockStatic(ConfigDataRetriever.class);
             MockedStatic<MainUI> mainUiStatic =
                     mockStatic(MainUI.class);
             MockedConstruction<LoginUI> loginCtor =
                     mockConstruction(LoginUI.class)) {

            cfg.when(() -> ConfigDataRetriever.get("deepl_api_key"))
                    .thenReturn("none");
            cfg.when(() -> ConfigDataRetriever.get("azure_api_key"))
                    .thenReturn("AZURE_OK");
            cfg.when(() -> ConfigDataRetriever.get("azure_region"))
                    .thenReturn("EASTUS");

            final MainUI mainUi = mock(MainUI.class);
            mainUiStatic
                    .when(() -> MainUI.createInstance("none",
                            "AZURE_OK", "EASTUS"))
                    .thenReturn(mainUi);

            newLauncher().start();

            verify(mainUi).setVisible(true);
            assert loginCtor.constructed().isEmpty();
        }
    }

    /**
     * Verifies that when no valid keys exist, the launcher shows the
     * LoginUI window.
     */
    @Test
    @DisplayName("start(): shows LoginUI when no valid keys")
    void start_NoKeys_ShowsLoginUI() {
        try (MockedStatic<ConfigDataRetriever> cfg =
                     mockStatic(ConfigDataRetriever.class);
             MockedStatic<MainUI> mainUiStatic =
                     mockStatic(MainUI.class);
             MockedConstruction<LoginUI> loginCtor =
                     mockConstruction(LoginUI.class)) {

            cfg.when(() -> ConfigDataRetriever.get("deepl_api_key"))
                    .thenReturn("none");
            cfg.when(() -> ConfigDataRetriever.get("azure_api_key"))
                    .thenReturn("");
            cfg.when(() -> ConfigDataRetriever.get("azure_region"))
                    .thenReturn(null);

            newLauncher().start();

            mainUiStatic.verifyNoInteractions();
            assert loginCtor.constructed().size() == 1;
            verify(loginCtor.constructed().get(0))
                    .setVisible(true);
        }
    }

    /**
     * Verifies error handling during configuration fetch:
     * exceptions are ignored and the decision falls back correctly.
     */
    @Test
    @DisplayName("start(): ignores get() exceptions and proceeds")
    void start_ConfigExceptions_Ignored() {
        try (MockedStatic<ConfigDataRetriever> cfg =
                     mockStatic(ConfigDataRetriever.class);
             MockedStatic<MainUI> mainUiStatic =
                     mockStatic(MainUI.class)) {

            cfg.when(() -> ConfigDataRetriever.get("deepl_api_key"))
                    .thenThrow(new RuntimeException("boom"));
            cfg.when(() -> ConfigDataRetriever.get("azure_api_key"))
                    .thenReturn("AZURE_OK");
            cfg.when(() -> ConfigDataRetriever.get("azure_region"))
                    .thenReturn("EASTUS");

            final MainUI mainUi = mock(MainUI.class);
            mainUiStatic
                    .when(() -> MainUI.createInstance("none",
                            "AZURE_OK", "EASTUS"))
                    .thenReturn(mainUi);

            newLauncher().start();

            verify(mainUi).setVisible(true);
        }
    }

    /**
     * Verifies that Azure without region is insufficient and the
     * launcher shows LoginUI.
     */
    @Test
    @DisplayName("start(): Azure key without region -> LoginUI")
    void start_AzureNoRegion_ShowsLoginUI() {
        try (MockedStatic<ConfigDataRetriever> cfg =
                     mockStatic(ConfigDataRetriever.class);
             MockedStatic<MainUI> mainUiStatic =
                     mockStatic(MainUI.class);
             MockedConstruction<LoginUI> loginCtor =
                     mockConstruction(LoginUI.class)) {

            cfg.when(() -> ConfigDataRetriever.get("deepl_api_key"))
                    .thenReturn("none");
            cfg.when(() -> ConfigDataRetriever.get("azure_api_key"))
                    .thenReturn("AZURE_OK");
            cfg.when(() -> ConfigDataRetriever.get("azure_region"))
                    .thenReturn("  ");

            newLauncher().start();

            mainUiStatic.verifyNoInteractions();
            assert loginCtor.constructed().size() == 1;
            verify(loginCtor.constructed().get(0))
                    .setVisible(true);
        }
    }
}
