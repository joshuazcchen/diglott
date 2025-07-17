package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Book.PageFactory;
import Configuration.ConfigDataRetriever;
import Translation.StoredWords;
import Translation.TranslatePage;
import Book.Page;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class MainUI extends JFrame {
    private JButton pickFileButton;
    private JButton startButton;
    private JButton closeButton;
    private JToggleButton darkModeToggle;
    private JButton logoutButton;

    private File selectedFile;
    private String bookText;
    private boolean darkMode;
    private List<Page> pages;

    private JComboBox<String> inputLangBox;
    private JComboBox<String> targetLangBox;
    private JComboBox<Integer> speedBox;

    private final StoredWords storedWords = new StoredWords();

    public MainUI(String apiKey) {
    private PageFactory pageFactory;
    private final StoredWords storedWords = new StoredWords();

    private static final Map<String, String> LANGUAGES = new LinkedHashMap<>() {{
        put("Arabic", "ar");
        put("Bulgarian", "bg");
        put("Czech", "cs");
        put("Danish", "da");
        put("German", "de");
        put("Greek", "el");
        put("English (Britain)", "en-gb");
        put("English (American)", "en-us");
        put("Spanish", "es");
        put("Estonian", "et");
        put("Finnish", "fi");
        put("French", "fr");
        put("Hungarian", "hu");
        put("Indonesian", "id");
        put("Italian", "it");
        put("Japanese", "ja");
        put("Korean", "ko");
        put("Lithuanian", "lt");
        put("Latvian", "lv");
        put("Norwegian (Bokmål)", "nb");
        put("Dutch", "nl");
        put("Polish", "pl");
        put("Portuguese (Brazilian)", "pt-br");
        put("Portuguese (European)", "pt-pt");
        put("Romanian", "ro");
        put("Russian", "ru");
        put("Slovak", "sk");
        put("Slovenian", "sl");
        put("Swedish", "sv");
        put("Turkish", "tr");
        put("Ukrainian", "uk");
        put("Chinese", "zh");
        put("Chinese (simplified)", "zh-hans");
        put("Chinese (traditional)", "zh-hant");
    }};

    public static MainUI createInstance(String apiKey) {
        String savedKey = null;
        try {
            savedKey = ConfigDataRetriever.get("api_key");
        } catch (Exception e) {
        }

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

    private MainUI(String apiKey) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setupUI();
    }

    private void setupUI() {
        String darkModeStr = ConfigDataRetriever.get("dark_mode");
        darkMode = (darkModeStr != null) ? Boolean.parseBoolean(darkModeStr) : false;

        setTitle("Diglott Translator");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dropdowns
        inputLangBox = new JComboBox<>(new String[]{"en"});
        targetLangBox = new JComboBox<>(new String[]{"fr", "en", "es", "de", "zh"});
        speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        inputLangBox.setSelectedItem("en");
        targetLangBox.setSelectedItem(ConfigDataRetriever.get("target_language"));
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());

        inputLangBox.setPreferredSize(new Dimension(80, 25));
        targetLangBox.setPreferredSize(new Dimension(80, 25));
        speedBox.setPreferredSize(new Dimension(50, 25));
        toLangButton = new JButton("To Language");
        toLangButton.setEnabled(true); // now enabled!

        // Buttons
        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setSelected(darkMode);
        logoutButton = new JButton("Logout");

        // Layout setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Compact language/speed panel
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        langPanel.add(new JLabel("From:"));
        langPanel.add(inputLangBox);
        langPanel.add(new JLabel("To:"));
        langPanel.add(targetLangBox);
        langPanel.add(new JLabel("Speed:"));
        langPanel.add(speedBox);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setMaximumSize(new Dimension(400, 100));
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        buttonPanel.add(fromLangButton);
        buttonPanel.add(toLangButton);
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
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("EPUB Files", "epub"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("All Supported", "txt", "epub"));

            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                    bookText = importer.importBook(selectedFile);
                    List<String> words = new ArrayList<>(Arrays.asList(bookText.split(" ")));
                    this.pages = PageFactory.paginate(words,
                            ConfigDataRetriever.getInt("page_length"));
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

            String inputLang = (String) inputLangBox.getSelectedItem();
            String targetLang = (String) targetLangBox.getSelectedItem();
            int speed = (int) speedBox.getSelectedItem();

            ConfigDataRetriever.set("target_language", targetLang);
            ConfigDataRetriever.set("speed", String.valueOf(speed));

            TranslatePage translatePage = new TranslatePage(storedWords);
            for (Page page : pages) {
                translatePage.translatePage(page);
            }

            startButton.setEnabled(false);
            pickFileButton.setEnabled(false);
            closeButton.setEnabled(false);

            new PageUI(pages).setVisible(true);
            new PageUI(pages, darkMode).setVisible(true);
        });

        closeButton.addActionListener(e -> dispose());

        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            ConfigDataRetriever.set("dark_mode", String.valueOf(darkMode));
            ConfigDataRetriever.saveConfig();
            applyTheme();
        });

        logoutButton.addActionListener(e -> {
            ConfigDataRetriever.set("api_key", "none");
            ConfigDataRetriever.saveConfig();
            dispose();
            new LoginUI().setVisible(true);
        });

        toLangButton.addActionListener(e -> {
            String[] languageNames = LANGUAGES.keySet().toArray(new String[0]);

            String currentTarget = ConfigDataRetriever.get("target_language");
            String currentSelection = languageNames[0];
            for (var entry : LANGUAGES.entrySet()) {
                if (entry.getValue().equals(currentTarget)) {
                    currentSelection = entry.getKey();
                    break;
                }
            }

            String selectedLanguage = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Target Language:",
                    "Target Language",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    languageNames,
                    currentSelection);

            if (selectedLanguage != null) {
                String selectedCode = LANGUAGES.get(selectedLanguage);
                ConfigDataRetriever.set("target_language", selectedCode);
                ConfigDataRetriever.saveConfig();
                JOptionPane.showMessageDialog(this,
                        "Target language set to: " + selectedLanguage + " (" + selectedCode + ")");
            }
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
    // Test commit to check GitHub tracking

}
