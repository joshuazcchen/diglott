package ui.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

import application.controller.TranslationController;
import configuration.ConfigDataRetriever;
import configuration.LanguageCodes;
import domain.model.Page;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for {@link MainUI}.
 *
 * <p>Goals:
 * -- Exercise all methods (createInstance, constructor, setupUI,
 *    arrangeLayout, addListeners, applyTheme).
 * -- Hit all major listener branches while avoiding side-effects
 *    like real file dialogs or extra windows.
 *
 * <p>Notes:
 * -- Uses reflection to access private fields/components.
 * -- Mocks {@code TranslationController} to control {@code loadBook()}.
 * -- Guards against truly headless environments.
 */
class MainUIAllMethodsTest {

    private MainUI ui;

    @BeforeEach
    void init() {
        // Conservative defaults that won't throw in constructor.
        // Ensure target_language is present in one of the sets.
        // Prefer DeepL if available, else Azure; else pick "en".
        String code = "en";
        if (!LanguageCodes.DEEPL_LANG_CODES.contains(code)
                && !LanguageCodes.AZURE_LANG_CODES.contains(code)) {
            // Best effort: if neither contains "en", fall back to any known.
            code = LanguageCodes.DEEPL_LANG_CODES.stream()
                    .findFirst().orElseGet(() ->
                            LanguageCodes.AZURE_LANG_CODES.stream()
                                    .findFirst().orElse("en"));
        }
        ConfigDataRetriever.set("target_language", code);
        ConfigDataRetriever.set("dark_mode", "false");
        ConfigDataRetriever.set("credentials_path", "");
        ConfigDataRetriever.saveConfig();
    }

    @AfterEach
    void cleanup() {
        if (ui != null) {
            ui.dispose();
        }
    }

    // ---------- Static factory ----------

    @Test
    void createInstance_WritesConfigAndBuilds() {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = MainUI.createInstance("dl-key", "az-key", "eastus");

        assertNotNull(ui);
        assertEquals("dl-key", ConfigDataRetriever.get("deepl_api_key"));
        assertEquals("az-key", ConfigDataRetriever.get("azure_api_key"));
        assertEquals("eastus", ConfigDataRetriever.get("azure_region"));
    }

    // ---------- Constructor branches ----------

    @Test
    void constructor_ThrowsOnUnsupportedTargetLanguage() {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ConfigDataRetriever.set("target_language", "xx-UNSUPPORTED");
        ConfigDataRetriever.saveConfig();

        assertThrows(IllegalArgumentException.class,
                () -> new MainUI("dl", "az", "region"));
    }

    @Test
    void constructor_SucceedsForDeepLOrAzure() {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        // Prefer a DeepL-supported code; otherwise Azure.
        String code = LanguageCodes.DEEPL_LANG_CODES.stream()
                .findFirst().orElseGet(() ->
                        LanguageCodes.AZURE_LANG_CODES.stream()
                                .findFirst().orElse("en"));

        ConfigDataRetriever.set("target_language", code);
        ConfigDataRetriever.saveConfig();

        ui = new MainUI("dl", "az", "region");
        assertTrue(ui.isShowing());
        assertTrue(ui.getWidth() > 0 && ui.getHeight() > 0);
    }

    // ---------- setupUI / arrangeLayout basic assertions ----------

    @Test
    void setupUI_BuildsComponents_WithExpectedState() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        // Access a few key fields created in setupUI/arrangeLayout
        final JComboBox<String> targetLangBox =
                getField(ui, "targetLangBox");
        final JButton pickFileButton = getField(ui, "pickFileButton");
        final JButton startButton = getField(ui, "startButton");
        final JButton saveButton = getField(ui, "saveButton");
        final JButton settingsButton = getField(ui, "settingsButton");
        final JButton logoutButton = getField(ui, "logoutButton");
        final JToggleButton darkModeToggle = getField(ui, "darkModeToggle");

        assertNotNull(targetLangBox);
        assertNotNull(pickFileButton);
        assertNotNull(startButton);
        assertNotNull(saveButton);
        assertNotNull(settingsButton);
        assertNotNull(logoutButton);
        assertNotNull(darkModeToggle);
    }

    // ---------- addListeners: pickFile flow ----------

    @Test
    void pickFileButton_NoResult_DoesNothing() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        // Swap in a mocked controller that returns null load result
        final TranslationController mocked = mock(TranslationController.class);
        when(mocked.loadBook()).thenReturn(null);
        setField(ui, "controller", mocked);

        final JButton pickFileButton = getField(ui, "pickFileButton");
        pickFileButton.doClick();

        // Expect internal fields still null
        assertNull(getField(ui, "pages"));
        assertNull(getField(ui, "bookText"));
        assertNull(getField(ui, "selectedFile"));
    }

    // ---------- addListeners: start flow (error branches only) ----------

    @Test
    void startButton_NoBook_ShowsWarning_NoCrash() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        // Ensure initial bookText is null
        setField(ui, "bookText", null);

        final JButton startButton = getField(ui, "startButton");
        // Should show dialog and return early (no exceptions)
        startButton.doClick();

        assertTrue(ui.isShowing());
    }

    @Test
    void startButton_InvalidLanguage_ShowsError_NoCrash() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        // Pretend a book is loaded
        setField(ui, "bookText", "x");
        setField(ui, "pages", List.of(mock(Page.class)));
        setField(ui, "selectedFile", new File("book.txt"));

        // Force an invalid selection: null item -> map returns null
        final JComboBox<String> targetLangBox = getField(ui, "targetLangBox");
        targetLangBox.setSelectedItem(null);

        final JButton startButton = getField(ui, "startButton");
        startButton.doClick();

        // We should still be on MainUI; no dispose on invalid selection
        assertTrue(ui.isShowing());
    }

    // ---------- addListeners: logout flow ----------

    @Test
    void logoutButton_ResetsConfig_AndDisposes() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        final JButton logoutButton = getField(ui, "logoutButton");
        logoutButton.doClick();

        assertEquals("none", ConfigDataRetriever.get("deepl_api_key"));
        assertEquals("none", ConfigDataRetriever.get("azure_api_key"));
        assertFalse(ui.isShowing());
    }

    // ---------- addListeners: dark mode toggle + applyTheme ----------

    @Test
    void darkModeToggle_TogglesAndSaves_ApplyThemeRuns() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless cannot create JFrame.");
        ui = new MainUI("dl", "az", "region");

        final JToggleButton darkModeToggle = getField(ui, "darkModeToggle");
        darkModeToggle.setSelected(true);

        // Listener writes config and calls applyTheme
        darkModeToggle.doClick(); // toggle off
        darkModeToggle.doClick(); // toggle on

        assertEquals("true", ConfigDataRetriever.get("dark_mode"));

        // Call private applyTheme directly to cover it too
        final Method applyTheme =
                MainUI.class.getDeclaredMethod("applyTheme");
        applyTheme.setAccessible(true);
        applyTheme.invoke(ui);
    }

    // ---------- helpers ----------

    @SuppressWarnings("unchecked")
    private static <T> T getField(final Object target, final String name)
            throws Exception {
        final Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return (T) f.get(target);
    }

    private static void setField(final Object target,
                                 final String name,
                                 final Object value) throws Exception {
        final Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}
