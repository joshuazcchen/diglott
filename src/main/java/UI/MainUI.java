package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Translation.TranslationHandler;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainUI extends JFrame {
    private JButton fromLangButton;
    private JButton toLangButton;
    private JButton pickFileButton;
    private JButton startButton;
    private JButton closeButton;
    private JToggleButton darkModeToggle;
    private JProgressBar progressBar;

    private File selectedFile;
    private String bookText = null;
    private boolean darkMode = false;

    private final String apiKey;

    // Constructor with API key
    public MainUI(String apiKey) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.apiKey = apiKey;
        setupUI();
    }

    private void setupUI() {
        setTitle("Diglott Translator");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fromLangButton = new JButton("From Language (Coming soon)");
        fromLangButton.setEnabled(false);

        toLangButton = new JButton("To Language (Coming soon)");
        toLangButton.setEnabled(false);

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        darkModeToggle = new JToggleButton("Dark Mode");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);


        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        buttonPanel.add(fromLangButton);
        buttonPanel.add(toLangButton);
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(progressBar);
        buttonPanel.add(closeButton);
        buttonPanel.add(darkModeToggle);

        add(buttonPanel);
        setVisible(true);
        addListeners();
        applyTheme();
    }

    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a text or EPUB file");

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("EPUB Files", "epub"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("All Supported", "txt", "epub"));

            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileHidingEnabled(false);
            fileChooser.setMultiSelectionEnabled(false);

            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                    bookText = importer.importBook(selectedFile);
                    JOptionPane.showMessageDialog(this, "Book loaded successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to load book.");
                }
            }
        });

        startButton.addActionListener(e -> {
            if (bookText == null) {
                JOptionPane.showMessageDialog(this, "Please load a book first.");
                return;
            }

            String[] words = bookText.split("\\s+");
            TranslationHandler translator = new TranslationHandler(apiKey);
            StringBuilder translated = new StringBuilder();

            progressBar.setVisible(true);
            progressBar.setValue(0);

            startButton.setEnabled(false);
            pickFileButton.setEnabled(false);
            closeButton.setEnabled(false);

        });
        closeButton.addActionListener(e -> {
            dispose();
        });
        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            applyTheme();
        });
    }
    private void applyTheme() {
        Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;

        applyThemeRecursive(getContentPane(), bg, fg);
        repaint();
    }

    private void applyThemeRecursive(Component component, Color bg, Color fg) {
        if (component instanceof JPanel || component instanceof JFrame || component instanceof JScrollPane) {
            component.setBackground(bg);
        }

        if (component instanceof AbstractButton button) {
            button.setBackground(bg);
            button.setForeground(fg);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }

        if (component instanceof JTextArea area) {
            area.setBackground(bg);
            area.setForeground(fg);
            area.setCaretColor(fg);
        }

        if (component instanceof JTextComponent field) {
            field.setBackground(bg);
            field.setForeground(fg);
            field.setCaretColor(fg);
        }

        if (component instanceof JLabel label) {
            label.setForeground(fg);
        }

        if (component instanceof JProgressBar bar) {
            bar.setBackground(bg);
            bar.setForeground(fg);
            bar.setBorderPainted(true);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child, bg, fg);
            }
        }
    }

}
