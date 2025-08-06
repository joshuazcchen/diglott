package ui.main;

import configuration.ConfigDataRetriever;
import configuration.LanguageCodes;
import ui.components.UIThemeManager;
import ui.login.LoginUI;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

/**
 * Main UI window for selecting book files, configuring translation settings, and launching page view.
 */
public class MainUI extends JFrame {

    /** Buttons. */
    private JButton pickFileButton;
    private JButton startButton;
    private JButton closeButton;
    private JButton logoutButton;
    private JButton settingsButton;

    /** Dark mode toggle. */
    private JToggleButton darkModeToggle;

    /** File currently selected by the user. */
    private File selectedFile;

    /** Raw text content of the loaded book. */
    private String bookText;

    /** Whether dark mode is enabled. */
    private boolean darkMode;

    /** Pages created from the loaded book. */
    private List<Page> pages;

    /** Stored translations. */
    private final StoredWords storedWords = new StoredWords();

    /** Translation controller. */
    private final TranslationController controller = new TranslationController();

    /** Speak controller. */
    private SpeakController speakController;

    /** Page translator use case. */
    private final TranslatePageUseCase translatorUseCase;

    /** Language selection boxes. */
    private JComboBox<String> inputLangBox;
    private JComboBox<String> targetLangBox;

    /**
     * Creates a MainUI instance with saved API key.
     *
     * @param apiKey API key for translation service
     * @return a new MainUI instance
     */
    public static MainUI createInstance(final String apiKey) {
        ConfigDataRetriever.set("api_key", apiKey);
        ConfigDataRetriever.saveConfig();
        return new MainUI(apiKey);
    }

    /**
     * Constructs a MainUI and sets up translators, UI, and theme.
     *
     * @param apiKey API key for translation service
     */
    MainUI(final String apiKey) {
        System.setProperty(
                "javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom",
                "net.sf.saxon.xpath.XPathFactoryImpl"
        );

        Translator translator = new TranslationHandler(apiKey, storedWords);
        WordTransliterator wordTransliterator = new TransliterationHandler();
        translatorUseCase = new TranslatePageInteractor(translator, wordTransliterator, storedWords);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // Ignore exception and keep default LookAndFeel
        }

        setupUI();
    }

    /** Initializes UI components and applies saved settings. */
    private void setupUI() {
        darkMode = Boolean.parseBoolean(ConfigDataRetriever.get("dark_mode"));
        setTitle("Diglott Translator");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputLangBox = new JComboBox<>(new String[]{"en"});
        targetLangBox = new JComboBox<>(LanguageCodes.LANGUAGES.keySet().toArray(new String[0]));

        inputLangBox.setSelectedItem("en-us");
        targetLangBox.setSelectedItem(
                LanguageCodes.REVERSELANGUAGES.get(ConfigDataRetriever.get("target_language"))
        );

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

    /** Arranges the UI layout with language, font, and control panels. */
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

    /** Adds listeners to UI components for button actions. */
    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            final var result = controller.loadBook();
            if (result != null) {
                pages = result.getPages();
                bookText = result.getText();
                selectedFile = result.getFile();
                JOptionPane.showMessageDialog(this, "Book loaded successfully!");
                pickFileButton.setText("Loaded: " + result.getFile().getName());
            }
        });

        startButton.addActionListener(e -> {
            if (bookText == null) {
                JOptionPane.showMessageDialog(this, "Please load a book first.");
                return;
            }

            ConfigDataRetriever.set("target_language",
                    LanguageCodes.LANGUAGES.get(targetLangBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();

            translatorUseCase.execute(pages.get(0));

            String credsPath = ConfigDataRetriever.get("credentials_path");
            SpeechManager speechManager = new SpeechManager(credsPath);
            SpeakWordsUseCase speakUseCase = new SpeakWordsInteractor(speechManager);
            speakController = new SpeakController(speakUseCase, speechManager);

            new PageUI(pages, darkMode, translatorUseCase, speakController).setVisible(true);
            dispose();
        });

        closeButton.addActionListener(e -> dispose());

        settingsButton.addActionListener(e -> new SettingsUi());

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

    /** Applies the current theme to the UI. */
    private void applyTheme() {
        UIThemeManager.applyTheme(getContentPane(), darkMode);
        repaint();
    }
}
