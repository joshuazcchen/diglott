package UI.main;

import Configuration.*;
import UI.components.UIThemeManager;
import UI.login.LoginUI;
import application.controller.SpeakController;
import application.controller.TranslationController;
import application.interactor.SpeakWordsInteractor;
import application.interactor.TranslatePageInteractor;
import application.usecase.SpeakWordsUseCase;
import application.usecase.TranslatePageUseCase;
import domain.gateway.ConfigGateway;
import domain.gateway.Speaker;
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import domain.model.Page;
import infrastructure.config.ConfigManager;
import infrastructure.persistence.StoredWords;
import infrastructure.translation.TranslationHandler;
import infrastructure.translation.TransliterationHandler;
import infrastructure.tts.SpeechManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainUI extends JFrame {
    private JButton pickFileButton, startButton, closeButton, logoutButton;
    private JToggleButton darkModeToggle;

    private File selectedFile;
    private String bookText;
    private boolean darkMode;
    private List<Page> pages;

    private final StoredWords storedWords = new StoredWords();
    private final TranslationController controller = new TranslationController();
    private TranslatePageUseCase translatorUseCase;
    private SpeakController speakController;

    private JComboBox<String> inputLangBox;
    private JComboBox<String> targetLangBox;
    private JComboBox<Integer> speedBox;
    private JComboBox<String> fontBox;
    private JCheckBox exponentialGrowthBox;
    private JCheckBox originalScriptBox;

    public static MainUI createInstance(String apiKey) {
        ConfigDataRetriever.set("api_key", apiKey);
        ConfigDataRetriever.saveConfig();
        return new MainUI(apiKey);
    }

    MainUI(String apiKey) {
        System.setProperty(
                "javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom",
                "net.sf.saxon.xpath.XPathFactoryImpl"
        );

        Translator translator = new TranslationHandler(apiKey, storedWords);
        WordTransliterator wordTransliterator = new TransliterationHandler();
        translatorUseCase = new TranslatePageInteractor(translator, wordTransliterator, storedWords);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        setupUI();
    }

    private void setupUI() {
        darkMode = Boolean.parseBoolean(ConfigDataRetriever.get("dark_mode"));
        setTitle("Diglott Translator");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputLangBox = new JComboBox<>(new String[]{"en"});
        targetLangBox = new JComboBox<>(LanguageCodes.LANGUAGES.keySet().toArray(new String[0]));
        speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        fontBox = new JComboBox<>(FontList.FONTS.keySet().toArray(new String[0]));
        exponentialGrowthBox = new JCheckBox();
        originalScriptBox = new JCheckBox();

        inputLangBox.setSelectedItem("en-us");
        targetLangBox.setSelectedItem(LanguageCodes.REVERSELANGUAGES.get(ConfigDataRetriever.get("target_language")));
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());
        fontBox.setSelectedItem(ConfigDataRetriever.get("font"));
        exponentialGrowthBox.setSelected(ConfigDataRetriever.getBool("increment"));
        originalScriptBox.setSelected(ConfigDataRetriever.getBool("original_script"));

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setSelected(darkMode);
        logoutButton = new JButton("Logout");

        arrangeLayout();
        setVisible(true);
        addListeners();
        applyTheme();
    }

    private void arrangeLayout() {
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
        speedFontRow.add(Box.createHorizontalStrut(30));
        speedFontRow.add(new JLabel("Font:"));
        speedFontRow.add(fontBox);
        speedFontRow.add(Box.createHorizontalStrut(30));
        speedFontRow.add(new JLabel("Incremental:"));
        speedFontRow.add(exponentialGrowthBox);
        speedFontRow.add(Box.createHorizontalStrut(30));
        speedFontRow.add(new JLabel("Original Script:"));
        speedFontRow.add(originalScriptBox);

        langPanel.add(languageRow);
        langPanel.add(speedFontRow);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(closeButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);
        bottomPanel.add(darkModeToggle);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(langPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            var result = controller.loadBook();
            if (result != null) {
                this.pages = result.pages;
                this.bookText = result.text;
                this.selectedFile = result.file;
                JOptionPane.showMessageDialog(this, "Book loaded successfully!");
                pickFileButton.setText("Loaded: " + result.file.getName());
            }
        });

        startButton.addActionListener(e -> {
            if (bookText == null) {
                JOptionPane.showMessageDialog(this, "Please load a book first.");
                return;
            }

            ConfigDataRetriever.set("increment", exponentialGrowthBox.isSelected());
            ConfigDataRetriever.set("original_script", originalScriptBox.isSelected());
            ConfigDataRetriever.set("target_language", LanguageCodes.LANGUAGES.get(targetLangBox.getSelectedItem()));
            ConfigDataRetriever.set("speed", String.valueOf(speedBox.getSelectedItem()));
            ConfigDataRetriever.set("font", FontList.FONTS.get(fontBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();

            translatorUseCase.execute(pages.get(0));

            // Set up TTS with stored credentials
            String credsPath = ConfigDataRetriever.get("google_credentials_path");
            SpeechManager speechManager = new SpeechManager(credsPath);
            SpeakWordsUseCase speakUseCase = new SpeakWordsInteractor(speechManager);
            speakController = new SpeakController(speakUseCase, speechManager);

            new PageUI(pages, darkMode, translatorUseCase, speakController).setVisible(true);
            dispose();
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
        UIThemeManager.applyTheme(getContentPane(), darkMode);
        repaint();
    }
}