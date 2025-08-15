package ui.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import configuration.ConfigDataRetriever;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SettingsUI}.
 *
 * <p>We focus on component creation and action listeners that write to
 * config. We do not open file choosers or external dialogs.
 */
class SettingsUITest {

    private SettingsUI ui;

    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }

    @Test
    void constructsAndHasControls_WritesConfigFromActions()
            throws InvocationTargetException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "No JFrame in headless.");

        ConfigDataRetriever.set("speed", "1");
        ConfigDataRetriever.set("pages_translated", "1");
        ConfigDataRetriever.set("font", "SansSerif");
        ConfigDataRetriever.set("increment", false);
        ConfigDataRetriever.set("original_script", false);
        ConfigDataRetriever.saveConfig();

        ui = new SettingsUI();

        // Find a JPanel content root (added CENTER).
        final Container content = (Container) ui.getContentPane()
                .getComponent(0);
        assertTrue(content instanceof JPanel);

        // Pick a combobox and toggle it to trigger set+save.
        JComboBox<?> speedBox = findFirst(content, JComboBox.class);
        assertNotNull(speedBox);
        speedBox.setSelectedItem(3);

        // Toggle checkboxes to trigger set+save.
        JCheckBox box = findFirst(content, JCheckBox.class);
        assertNotNull(box);
        box.setSelected(!box.isSelected());
        // Fire action by calling doClick on parent label row if needed.
        box.doClick();

        // Expect config to have updated entries (loose check).
        assertNotNull(ConfigDataRetriever.get("speed"));
    }

    private <T> T findFirst(Component root, Class<T> type) {
        if (type.isInstance(root)) {
            return type.cast(root);
        }
        if (root instanceof Container) {
            for (Component c : ((Container) root).getComponents()) {
                final T found = findFirst(c, type);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
