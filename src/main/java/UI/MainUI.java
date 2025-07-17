package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Book.PageFactory;
import Configuration.ConfigDataRetriever;
import Configuration.LanguageCodes;
import Translation.StoredWords;
import Translation.TranslatePage;
import Book.Page;

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
        setupUI();
    }

    private void setupUI() {

        setTitle("Diglott Translator");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputLangBox = new JComboBox<>(new String[]{"en"}); // TODO: support LANGUAGES.keySet() if needed
        targetLangBox = new JComboBox<>(LanguageCodes.LANGUAGES.keySet().toArray(new String[0]));
        speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        inputLangBox.setSelectedItem("en");
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());

        // Get readable name from code
        String currentTarget = ConfigDataRetriever.get("target_language");
        for (String name : LanguageCodes.LANGUAGES.keySet()) {
            if (LanguageCodes.LANGUAGES.get(name).equals(currentTarget)) {
                targetLangBox.setSelectedItem(name);
                break;
            }
        }

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        logoutButton = new JButton("Logout");
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setSelected(darkMode);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        langPanel.add(new JLabel("From:"));
        langPanel.add(inputLangBox);
        langPanel.add(new JLabel("To:"));
        langPanel.add(targetLangBox);
        langPanel.add(new JLabel("Speed:"));
        langPanel.add(speedBox);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(darkModeToggle);
        buttonPanel.add(logoutButton);

        mainPanel.add(langPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);

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
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("All Supported", "txt", "epub"));

            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                    bookText = importer.importBook(selectedFile);
                    pages = PageFactory.paginate(Arrays.asList(bookText.split(" ")), ConfigDataRetriever.getInt("page_length"));
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

            String targetLang = LanguageCodes.LANGUAGES.get((String) targetLangBox.getSelectedItem());
            int speed = (int) speedBox.getSelectedItem();

            ConfigDataRetriever.set("target_language", targetLang);
            ConfigDataRetriever.set("speed", String.valueOf(speed));

            TranslatePage translator = new TranslatePage(storedWords);
            for (Page page : pages) translator.translatePage(page);

            pickFileButton.setEnabled(false);
            closeButton.setEnabled(false);
            startButton.setEnabled(false);

            new PageUI(pages, darkMode).setVisible(true);
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

    private void applyThemeRecursive(Component comp, Color bg, Color fg) {
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child, bg, fg);
            }
        }

        if (comp instanceof AbstractButton b) {
            b.setBackground(bg);
            b.setForeground(fg);
            b.setOpaque(true);
        } else if (comp instanceof JTextComponent f) {
            f.setBackground(bg);
            f.setForeground(fg);
            f.setCaretColor(fg);
        } else if (comp instanceof JLabel l) {
            l.setForeground(fg);
        }
    }
}
