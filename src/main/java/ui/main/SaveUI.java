package ui.main;

import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import configuration.ConfigDataRetriever;
import domain.model.Book;
import infrastructure.exporter.SaveBook;
import infrastructure.importer.LoadBook;
import ui.components.UIThemeManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.nio.file.Path;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * UI window for saving and viewing saved books.
 */
public class SaveUI extends JFrame {

    /** the width. */
    private static final int WIDTH = 600;
    /** the height. */
    private static final int HEIGHT = 750;
    /**
     * opens a saved file.
     * @param darkModeEnabled checks to see if already dark mode.
     * @param translatorUseCase translator use case.
     * @param speakCtrl the speak controller.
     */
    public SaveUI(final boolean darkModeEnabled,
                  final TranslatePageUseCase translatorUseCase,
                  final SpeakController speakCtrl) {
        setTitle("Saved Books");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Load all .dig files
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        Path saveDir = SaveBook.getSaveDirectory();
        File[] digFiles = saveDir.toFile().listFiles((dir,
                                                      name)
                -> name.endsWith(".dig"));

        if (digFiles != null) {
            for (File file : digFiles) {
                JButton fileButton = new JButton(file.getName());
                fileButton.setAlignmentX(Component.LEFT_ALIGNMENT);

                fileButton.addActionListener(e -> {
                    try {
                        Book loaded = LoadBook.importBook(file);
                        dispose();
                        new PageUI(loaded, darkModeEnabled, translatorUseCase,
                                speakCtrl).setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "Failed to load: " + file.getName(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                filePanel.add(fileButton);
            }
        } else {
            filePanel.add(new JLabel("No saved books found."));
        }

        JScrollPane scrollPane = new JScrollPane(filePanel);
        add(scrollPane, BorderLayout.CENTER);

        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            dispose();
            MainUI.createInstance(ConfigDataRetriever.get("deepl_api_key"),
                    ConfigDataRetriever.get("azure_api_key"),
                    ConfigDataRetriever.get("azure_region")).setVisible(true);
        });
        add(closeButton);
        if (darkModeEnabled) {
            UIThemeManager.applyTheme(getContentPane(), darkModeEnabled);
            repaint();
        }
        setVisible(true);
    }
}
