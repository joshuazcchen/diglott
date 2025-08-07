package ui.main;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * Blank UI window for saving and viewing saved books.
 */
public class SaveUI extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 750;

    public SaveUI(final boolean darkModeEnabled) {
        setTitle("Saved Books");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TODO: Add components here

        setVisible(true);
    }
}
