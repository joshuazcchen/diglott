package ui.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Smoke test for {@link SaveUI}.
 *
 * <p>We only assert the frame constructs and shows. The list of files
 * depends on the user's save directory, so we do not assert content.
 */
class SaveUITest {

    private SaveUI ui;

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void constructs_AndShows() {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "No JFrame in headless.");

        ui = new SaveUI(false, page -> { /* no-op */ }, null);

        assertTrue(ui.isShowing());
        assertTrue(ui.getWidth() > 0 && ui.getHeight() > 0);
    }
}
