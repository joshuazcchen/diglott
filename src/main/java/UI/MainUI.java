package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Book.PageFactory;
import Book.Page;
import Configuration.ConfigDataRetriever;
import Configuration.LanguageCodes;
import Configuration.FontList;
import Translation.StoredWords;
import Translation.TranslatePage;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainUI extends JFrame {
    private JButton pickFileButton, startButton, closeButton, logoutButton;
    private JToggleButton darkModeToggle;

    private File selectedFile;
    private String bookText;
    private boolean darkMode;
    private List<Page> pages;

    private JComboBox<String> inputLangBox;
    private JComboBox<String> targetLangBox;
    private JComboBox<Integer> speedBox;
    private JComboBox<String> fontBox;

    private final StoredWords storedWords = new StoredWords();

    public static MainUI createInstance(String apiKey) {
        String savedKey = null;
        try {
            savedKey = ConfigDataRetriever.get("api_key");
        } catch (Exception ignored) {}

        if (savedKey != null && !savedKey.trim().isEmpty() && !savedKey.equals("none")) {
            return new MainUI(savedKey);
        } else if (apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("none")) {
            ConfigDataRetriever.set("api_key", apiKey);
            ConfigDataRetriever.saveConfig();
            return new MainUI(apiKey);
        } else {
            return new MainUI("none");
        }
    }

    MainUI(String apiKey) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        ConfigDataRetriever.set("api_key", apiKey);
        setupUI();
    }

    private void setupUI() {
        String darkModeStr = ConfigDataRetriever.get("dark_mode");
        darkMode = darkModeStr != null && Boolean.parseBoolean(darkModeStr);

        setTitle("Diglott Translator");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputLangBox = new JComboBox<>(new String[]{"en"}); // TODO: support LANGUAGES.keySet() if needed
        targetLangBox = new JComboBox<>(LanguageCodes.LANGUAGES.keySet().toArray(new String[0]));
        speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        fontBox = new JComboBox<>(FontList.FONTS.keySet().toArray(new String[0]));

        inputLangBox.setSelectedItem("en-us");  // Default input language
        targetLangBox.setSelectedItem(ConfigDataRetriever.get("target_language"));
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());
        fontBox.setSelectedItem(ConfigDataRetriever.get("font"));

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setSelected(darkMode);
        logoutButton = new JButton("Logout");

        JPanel langPanel = new JPanel();
        langPanel.setLayout(new BoxLayout(langPanel, BoxLayout.Y_AXIS));
        langPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel languageRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        languageRow.add(new JLabel("From:"));
        languageRow.add(inputLangBox);
        languageRow.add(Box.createHorizontalStrut(20));
        languageRow.add(new JLabel("To:"));
        languageRow.add(targetLangBox);

        JPanel speedFontRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        speedFontRow.add(new JLabel("Speed:"));
        speedFontRow.add(speedBox);
        speedFontRow.add(Box.createHorizontalStrut(30)); // optional spacing
        speedFontRow.add(new JLabel("Font:"));
        speedFontRow.add(fontBox);

        langPanel.add(languageRow);
        langPanel.add(speedFontRow);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(darkModeToggle);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(langPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        addListeners();
        applyTheme();
    }

    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a text or EPUB file");

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("EPUB Files", "epub"));

            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                    bookText = importer.importBook(selectedFile);
                    List<String> words = new ArrayList<>(Arrays.asList(bookText.split(" ")));
                    this.pages = PageFactory.paginate(words, ConfigDataRetriever.getInt("page_length"));
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

            String targetLang = LanguageCodes.LANGUAGES.get(targetLangBox.getSelectedItem());
            int speed = (int) speedBox.getSelectedItem();
            String font = FontList.FONTS.get(fontBox.getSelectedItem());

            ConfigDataRetriever.set("target_language", targetLang);
            ConfigDataRetriever.set("speed", String.valueOf(speed));
            ConfigDataRetriever.set("font", font);
            ConfigDataRetriever.saveConfig();

            TranslatePage translatePage = new TranslatePage(storedWords);
            for (Page page : pages) {
                translatePage.translatePage(page);
            }

            startButton.setEnabled(false);
            pickFileButton.setEnabled(false);
            closeButton.setEnabled(false);

            setVisible(false);
            new PageUI(pages, darkMode, this).setVisible(true);
        });

        closeButton.addActionListener(e -> dispose());

        logoutButton.addActionListener(e -> {
            ConfigDataRetriever.set("api_key", "none");
            ConfigDataRetriever.saveConfig();
            dispose();
            new LoginUI().setVisible(true);
        });

        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            ConfigDataRetriever.set("dark_mode", String.valueOf(darkMode));
            ConfigDataRetriever.saveConfig();
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