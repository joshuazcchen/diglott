package ui.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

/**
 * Utility class for applying a light or dark theme to Swing components.
 */
public final class UIThemeManager {

    private UIThemeManager() {
        // Prevent instantiation
    }

    /**
     * Applies a light or dark theme to the
     * given root container and all its children.
     *
     * @param root     the root container to apply the theme to
     * @param darkMode {@code true} for dark mode, {@code false} for light mode
     */
    public static void applyTheme(final Container root,
                                  final boolean darkMode) {
        final Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        final Color fg = darkMode ? Color.WHITE : Color.BLACK;
        applyRecursive(root, bg, fg);
    }

    /**
     * Recursively applies the background and foreground
     * colors to a component and its children.
     *
     * @param component the component to style
     * @param bg        the background color
     * @param fg        the foreground color
     */
    private static void applyRecursive(final Component component,
                                       final Color bg, final Color fg) {
        if (component instanceof JComponent) {
            component.setBackground(bg);
            component.setForeground(fg);
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyRecursive(child, bg, fg);
            }
        }
    }
}
