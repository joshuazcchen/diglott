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
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import domain.model.Page;
import infrastructure.persistence.StoredWords;
import infrastructure.translation.TranslationHandler;
import infrastructure.translation.TransliterationHandler;
import infrastructure.tts.SpeechManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainUI extends JFrame {
    private JButton pickFileButton, startButton, closeButton, logoutButton, settingsButton;
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

        inputLangBox.setSelectedItem("en-us");
        targetLangBox.setSelectedItem(LanguageCodes.REVERSELANGUAGES.get(ConfigDataRetriever.get("target_language")));

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        settingsButton = new JButton("Settings");
        logoutButton = new JButton("Logout");
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setSelected(darkMode);

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

        langPanel.add(languageRow);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(settingsButton);

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

            ConfigDataRetriever.set("target_language", LanguageCodes.LANGUAGES.get(targetLangBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();

            translatorUseCase.execute(pages.get(0));

            String credsPath = ConfigDataRetriever.get("google_credentials_path");
            SpeechManager speechManager = new SpeechManager(credsPath);
            SpeakWordsUseCase speakUseCase = new SpeakWordsInteractor(speechManager);
            speakController = new SpeakController(speakUseCase, speechManager);

            new PageUI(pages, darkMode, translatorUseCase, speakController).setVisible(true);
            dispose();
        });

        closeButton.addActionListener(e -> dispose());

        settingsButton.addActionListener(e -> new SettingsUI());

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
