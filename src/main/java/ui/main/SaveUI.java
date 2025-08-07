package ui.main;

import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.model.Book;
import infrastructure.exporter.SaveBook;
import infrastructure.importer.LoadBook;

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
 * UI window for listing, loading, and opening saved books.
 *
 * <p>Responsibility is limited to Swing concerns and wiring user actions
 * to use cases/controllers passed in via the constructor.</p>
 */
public final class SaveUI extends JFrame {

    /** Width of the window in pixels. */
    private static final int WIDTH = 600;

    /** Height of the window in pixels. */
    private static final int HEIGHT = 750;

    /**
     * Creates a new {@code SaveUI} window and populates it with buttons for
     * each saved book found in the save directory.
     *
     * @param darkModeEnabled whether dark mode is enabled
     * @param translatorUseCase use case for translating pages
     * @param speakController controller for text‑to‑speech actions
     */
    public SaveUI(final boolean darkModeEnabled,
                  final TranslatePageUseCase translatorUseCase,
                  final SpeakController speakController) {
        super("Saved Books");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        final JPanel filePanel = createFilePanel(
                SaveBook.getSaveDirectory(),
                darkModeEnabled,
                translatorUseCase,
                speakController
        );

        final JScrollPane scrollPane = new JScrollPane(filePanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Builds the panel that lists all saved book files as buttons.
     *
     * @param saveDir the directory where .dig files are stored
     * @param darkModeEnabled whether dark mode is enabled
     * @param translatorUseCase use case for translating pages
     * @param speakController controller for text‑to‑speech actions
     * @return a populated panel
     */
    private JPanel createFilePanel(final Path saveDir,
                                   final boolean darkModeEnabled,
                                   final TranslatePageUseCase translatorUseCase,
                                   final SpeakController speakController) {
        final JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        final File[] digFiles = saveDir
                .toFile()
                .listFiles((dir, name) -> name.endsWith(".dig"));

        if (digFiles == null || digFiles.length == 0) {
            filePanel.add(new JLabel("No saved books found."));
            return filePanel;
        }

        for (final File file : digFiles) {
            final JButton button = createFileButton(
                    file,
                    darkModeEnabled,
                    translatorUseCase,
                    speakController
            );
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            filePanel.add(button);
        }

        return filePanel;
    }

    /**
     * Creates a button that loads the
     * given file and opens it in {@link PageUI}.
     *
     * @param file the saved book file
     * @param darkModeEnabled whether dark mode is enabled
     * @param translatorUseCase use case for translating pages
     * @param speakController controller for text‑to‑speech actions
     * @return a configured button
     */
    private JButton createFileButton(final File file,
                                     final boolean darkModeEnabled,
                                     final TranslatePageUseCase
                                             translatorUseCase,
                                     final SpeakController speakController) {
        final JButton fileButton = new JButton(file.getName());

        fileButton.addActionListener(event -> {
            try {
                final Book loaded = LoadBook.importBook(file);
                dispose();
                final PageUI pageUi = new PageUI(
                        loaded,
                        darkModeEnabled,
                        translatorUseCase,
                        speakController
                );
                pageUi.setVisible(true);
            } catch (final Exception ex) {
                showLoadError(file, ex);
            }
        });

        return fileButton;
    }

    /**
     * Shows a load error dialog for a failed book import.
     *
     * @param file the file that failed to load
     * @param ex   the thrown exception
     */
    private void showLoadError(final File file, final Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "Failed to load: " + file.getName(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
