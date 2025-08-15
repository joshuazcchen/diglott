package ui.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Smoke tests for {@link DeepLLoginUI}.
 *
 * <p>These tests only verify that the frame can be constructed and shown.
 * They intentionally avoid clicking the Login button to prevent real
 * network calls.</p>
 */
class DeepLLoginTest {

    private DeepLLoginUI ui;

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void constructsFrame_WithCallback_AndShows() {
        // Guard for CI environments that are truly headless.
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless environment cannot create JFrame.");

        // Provide a no-op callback; signature depends on your interface.
        final LoginCallback cb = key -> { /* no-op */ };

        ui = new DeepLLoginUI(cb);

        assertEquals("Diglott Login", ui.getTitle(),
                "Window title should match.");
        assertTrue(ui.isShowing(), "Login frame should be visible.");
        assertTrue(ui.getWidth() > 0 && ui.getHeight() > 0,
                "Frame should have non-zero size.");
    }
}
