package ui.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import application.controller.SpeakController;
import configuration.ConfigDataRetriever;
import domain.model.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SpeakUI}.
 *
 * <p>Covers:
 * - HTML stripping and parentheses extraction via reflection.
 * - Grid population rules: dedupe, punctuation strip, skip empties.
 * - Click calls SpeakController with lang code.
 */
class SpeakUITest {

    private SpeakUI ui;

    @BeforeEach
    void setUp() {
        ConfigDataRetriever.set("target_language", "en-US");
        ConfigDataRetriever.set("original_script", true);
        ConfigDataRetriever.saveConfig();
    }

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void privateHelpers_WorkViaReflection()
            throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "No JFrame in headless.");

        final Page page = mock(Page.class);
        when(page.getWords()).thenReturn(List.of("x"));

        final SpeakController sc = mock(SpeakController.class);
        ui = new SpeakUI(page, sc, true);

        final Method strip =
                SpeakUI.class.getDeclaredMethod("stripHtml", String.class);
        strip.setAccessible(true);
        final String s = (String) strip.invoke(ui,
                "   <b>hi</b>&nbsp;there  ");
        assertEquals("hi there", s);

        final Method paren =
                SpeakUI.class.getDeclaredMethod(
                        "extractInsideParentheses", String.class);
        paren.setAccessible(true);
        assertEquals("world", paren.invoke(ui, "hello (world) !"));
        assertEquals("", paren.invoke(ui, "no parens here"));
    }

    @Test
    void populate_AddsButtons_DedupesAndCleans_AndSpeaksOnClick()
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "No JFrame in headless.");

        // Preserve original => words look like "orig (trans)"
        ConfigDataRetriever.set("original_script", true);
        ConfigDataRetriever.saveConfig();

        final Page page = mock(Page.class);
        when(page.getWords()).thenReturn(List.of(
                "<i>bonjour</i> (hello!)",
                "salut (hello!)",     // duplicate translated
                "merci (thanks.)",
                "badformat",          // skipped: no parens
                "empty ()",           // empty after clean -> skip
                "punct (wow!!!)"      // cleans punctuation
        ));

        final SpeakController sc = mock(SpeakController.class);

        ui = new SpeakUI(page, sc, false);

        // Find grid panel: the content component inside the scroll pane.
        final JPanel grid = (JPanel) ((javax.swing.JScrollPane)
                ui.getContentPane().getComponent(0))
                .getViewport().getView();

        // We expect unique translated words: hello, thanks, wow
        int buttonCount = 0;
        for (var comp : grid.getComponents()) {
            if (comp instanceof JButton) {
                buttonCount++;
            }
        }
        assertEquals(3, buttonCount);

        // Click a button and assert controller is invoked.
        JButton first = (JButton) grid.getComponent(0);
        first.doClick();
        verify(sc, atLeastOnce()).speakWord(anyString(), eq("en-US"));
    }
}
