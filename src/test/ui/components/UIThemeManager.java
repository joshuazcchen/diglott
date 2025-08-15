package ui.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UIThemeManager}.
 */
class UIThemeManagerTest {

    /**
     * Verifies dark mode colors are applied to all Swing descendants.
     */
    @Test
    void applyTheme_AppliesDarkModeRecursively() {
        final JPanel root = new JPanel();
        final JPanel inner = new JPanel();
        final JLabel leaf = new JLabel("x");
        final JButton btn = new JButton("ok");

        inner.add(leaf);
        root.add(inner);
        root.add(btn);

        UIThemeManager.applyTheme(root, true);

        final Color bg = Color.DARK_GRAY;
        final Color fg = Color.WHITE;

        assertEquals(bg, root.getBackground());
        assertEquals(fg, root.getForeground());
        assertEquals(bg, inner.getBackground());
        assertEquals(fg, inner.getForeground());
        assertEquals(bg, leaf.getBackground());
        assertEquals(fg, leaf.getForeground());
        assertEquals(bg, btn.getBackground());
        assertEquals(fg, btn.getForeground());
    }

    /**
     * Verifies light mode colors are applied to all Swing descendants.
     */
    @Test
    void applyTheme_AppliesLightModeRecursively() {
        final JPanel root = new JPanel();
        final JPanel inner = new JPanel();
        final JLabel leaf = new JLabel("x");

        inner.add(leaf);
        root.add(inner);

        UIThemeManager.applyTheme(root, false);

        final Color bg = Color.WHITE;
        final Color fg = Color.BLACK;

        assertEquals(bg, root.getBackground());
        assertEquals(fg, root.getForeground());
        assertEquals(bg, inner.getBackground());
        assertEquals(fg, inner.getForeground());
        assertEquals(bg, leaf.getBackground());
        assertEquals(fg, leaf.getForeground());
    }

    /**
     * If the root is an AWT {@link Container} (not a {@code JComponent}),
     * the root colors are not changed, but Swing children are themed.
     */
    @Test
    void applyTheme_DoesNotChangeNonJComponentRoot() {
        final Container root = new Container(); // not a JComponent
        final JPanel child = new JPanel();
        root.add(child);

        final Color originalBg = root.getBackground();
        final Color originalFg = root.getForeground();

        UIThemeManager.applyTheme(root, true);

        // Root unchanged because it's not a JComponent.
        assertEquals(originalBg, root.getBackground());
        assertEquals(originalFg, root.getForeground());

        // Swing child themed.
        assertEquals(Color.DARK_GRAY, child.getBackground());
        assertEquals(Color.WHITE, child.getForeground());

        // Sanity check the child is actually updated vs. defaults.
        assertNotEquals(originalBg, child.getBackground());
    }
}
