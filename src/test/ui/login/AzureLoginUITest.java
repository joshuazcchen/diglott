package ui.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Basic smoke tests for {@link AzureLoginUI} that avoid driving
 * network and event handlers. These ensure the frame constructs
 * successfully and basic properties are set.
 */
class AzureLoginUITest {

    private AzureLoginUI ui;

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void constructsFrame_WithCallback() {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless environment cannot create JFrame.");

        final AzureLoginUI.AzureLoginCallback cb =
                (key, region) -> { /* no-op */ };

        ui = new AzureLoginUI(cb);

        assertEquals("Diglott Login", ui.getTitle());
        assertTrue(ui.isShowing(), "Login frame should be visible.");
        assertTrue(ui.getWidth() > 0 && ui.getHeight() > 0,
                "Frame should have non-zero size.");
    }
}
