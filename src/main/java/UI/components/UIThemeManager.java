package UI.components;

import javax.swing.*;
import java.awt.*;

public class UIThemeManager {

    public static void applyTheme(Container root, boolean darkMode) {
        Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        applyRecursive(root, bg, fg);
    }

    private static void applyRecursive(Component component, Color bg, Color fg) {
        if (component instanceof JComponent jc) {
            jc.setBackground(bg);
            jc.setForeground(fg);
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyRecursive(child, bg, fg);
            }
        }
    }
}
