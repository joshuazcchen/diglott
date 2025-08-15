package ui.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LoginUI}.
 *
 * <p>These tests reflectively toggle the internal state flags and call
 * the private {@code updateContinueButton} to verify the enabling logic
 * without driving the actual dialog flows.</p>
 */
class LoginUITest {

    private LoginUI ui;

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void continueEnabled_WhenEitherProviderLoggedIn() throws Exception {
        // Skip if truly headless (many CI images are not fully headless,
        // but we guard just in case).
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Headless environment cannot create JFrame.");

        ui = new LoginUI();

        // Reflect fields
        final Field azureLoggedInF =
                LoginUI.class.getDeclaredField("azureLoggedIn");
        final Field deepLLoggedInF =
                LoginUI.class.getDeclaredField("deepLLoggedIn");
        final Field continueButtonF =
                LoginUI.class.getDeclaredField("continueButton");
        azureLoggedInF.setAccessible(true);
        deepLLoggedInF.setAccessible(true);
        continueButtonF.setAccessible(true);

        // Reflect private method
        final Method updateContinueButtonM =
                LoginUI.class.getDeclaredMethod("updateContinueButton");
        updateContinueButtonM.setAccessible(true);

        final JButton continueBtn =
                (JButton) continueButtonF.get(ui);

        // Neither logged in => disabled
        azureLoggedInF.set(ui, false);
        deepLLoggedInF.set(ui, false);
        updateContinueButtonM.invoke(ui);
        assertFalse(continueBtn.isEnabled(),
                "Continue should be disabled if neither is logged in.");

        // Azure only => enabled
        azureLoggedInF.set(ui, true);
        deepLLoggedInF.set(ui, false);
        updateContinueButtonM.invoke(ui);
        assertTrue(continueBtn.isEnabled(),
                "Continue should be enabled if Azure is logged in.");

        // DeepL only => enabled
        azureLoggedInF.set(ui, false);
        deepLLoggedInF.set(ui, true);
        updateContinueButtonM.invoke(ui);
        assertTrue(continueBtn.isEnabled(),
                "Continue should be enabled if DeepL is logged in.");

        // Both => enabled
        azureLoggedInF.set(ui, true);
        deepLLoggedInF.set(ui, true);
        updateContinueButtonM.invoke(ui);
        assertTrue(continueBtn.isEnabled(),
                "Continue should be enabled if both are logged in.");
    }
}
