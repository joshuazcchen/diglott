package ui.main;

import infrastructure.exporter.SaveBook;
import infrastructure.importer.LoadBook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * UI window for saving and viewing saved books.
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

        // Load all .dig files
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        Path saveDir = SaveBook.getSaveDirectory();
        File[] digFiles = saveDir.toFile().listFiles((dir, name) -> name.endsWith(".dig"));

        if (digFiles != null) {
            for (File file : digFiles) {
                JButton fileButton = new JButton(file.getName());
                fileButton.setAlignmentX(Component.LEFT_ALIGNMENT);

                fileButton.addActionListener(e -> {
                    try {
                        LoadBook.importBook(file);
                        JOptionPane.showMessageDialog(this, "Loaded: " + file.getName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to load: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                filePanel.add(fileButton);
            }
        } else {
            filePanel.add(new JLabel("No saved books found."));
        }

        JScrollPane scrollPane = new JScrollPane(filePanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
