package ui.main;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import application.controller.SpeakController;
import application.controller.TranslationController;
import application.interactor.SpeakWordsInteractor;
import application.interactor.TranslatePageInteractor;
import application.usecase.SpeakWordsUseCase;
import application.usecase.TranslatePageUseCase;
import application.usecase.ImportBookUseCase;              // <-- added
import configuration.ConfigDataRetriever;
import configuration.LanguageCodes;
import domain.gateway.Translator;
import domain.gateway.WordTransliterator;
import domain.gateway.ImporterSelector;                    // <-- added
import domain.model.Page;
import infrastructure.importer.FactorySelector;            // <-- added
import infrastructure.persistence.StoredWords;
import infrastructure.translation.DeepLTranslationHandler;
import infrastructure.translation.AzureTranslationHandler;
import infrastructure.translation.TransliterationHandler;
import infrastructure.tts.SpeechManager;
import ui.components.UIThemeManager;
import ui.login.LoginUI;

import java.util.List;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import domain.gateway.Speaker;

import application.interactor.ImportBookInteractor;        // <-- added
import domain.model.Book;

/**
 * Main UI window for selecting book files, configuring
 * translation settings, and launching page view.
 */
public class MainUI extends JFrame {

    /** Buttons. */
    private JButton pickFileButton;

    /** Start button. */
    private JButton startButton;

    /** Close button. */
    private JButton saveButton;

    /** Logout button. */
    private JButton logoutButton;

    /** Settings button. */
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
    private final TranslationController controller;        // <-- changed

    /** Speak controller. */
    private SpeakController speakController;

    /** Page translator use case. */
    private final TranslatePageUseCase translatorUseCase;

    /** Input language box. */
    private JComboBox<String> inputLangBox;

    /** Target language box. */
    private JComboBox<String> targetLangBox;

    /** Ui width. */
    private static final int WIDTH = 700;

    /** UI height. */
    private static final int HEIGHT = 300;

    /** Small UI element size. */
    private static final int SMALLUIELEMENT = 10;

    /** Number of rows. */
    private static final int ROWS = 2;

    /** Number of columns. */
    private static final int COLS = 2;

    /** Medium UI element size. */
    private static final int MEDIUMUIELEMENT = 20;

    /**
     * Creates a MainUI instance with saved API key.
     *
     * @param deepLApiKey API key for translation service
     * @param azureApiKey API key for azure
     * @param azureRegion Region code for azure
     * @return a new MainUI instance
     */
    public static MainUI createInstance(final String deepLApiKey,
                                        final String azureApiKey,
                                        final String azureRegion) {
        ConfigDataRetriever.set("deepl_api_key", deepLApiKey);
        ConfigDataRetriever.set("azure_api_key", azureApiKey);
        ConfigDataRetriever.set("azure_region", azureRegion);
        ConfigDataRetriever.saveConfig();
        System.out.println("DeepL key: "
                + ConfigDataRetriever.get("deepl_api_key"));
        System.out.println("Azure key: "
                + ConfigDataRetriever.get("azure_api_key"));
        System.out.println("Azure region: "
                + ConfigDataRetriever.get("azure_region"));
        return new MainUI(deepLApiKey, azureApiKey, azureRegion);
    }

    /**
     * Constructs a MainUI and sets up translators, UI, and theme.
     *
     * @param deepLApiKey DeepL API key for translation service
     * @param azureApiKey Azure API key for translation service
     * @param azureRegion Azure region code for translation service
     */
    MainUI(final String deepLApiKey, final String azureApiKey,
           final String azureRegion) {
        System.setProperty(
                "javax.xml.xpath.XPathFactory:"
                        + "http://java.sun.com/jaxp/xpath/dom",
                "net.sf.saxon.xpath.XPathFactoryImpl"
        );

        String targetLanguage = ConfigDataRetriever.get("target_language");
        Translator translator;

        if (LanguageCodes.DEEPL_LANG_CODES.contains(targetLanguage)) {
            translator = new DeepLTranslationHandler(deepLApiKey, storedWords);
        } else if (LanguageCodes.AZURE_LANG_CODES.contains(targetLanguage)) {
            translator = new AzureTranslationHandler(azureApiKey, azureRegion,
                    storedWords);
        } else {
            throw new IllegalArgumentException("Unsupported target language "
                    + "code: " + targetLanguage);
        }
        WordTransliterator wordTransliterator = new TransliterationHandler();
        translatorUseCase = new TranslatePageInteractor(
                translator, wordTransliterator, storedWords);

        // --- NEW: build the ImportBook use case and controller wiring ---
        final ImporterSelector selector = new FactorySelector();
        final ImportBookUseCase importUc =
                new ImportBookInteractor(selector);
        this.controller = new TranslationController(importUc);
        // ----------------------------------------------------------------

        try {
            UIManager.setLookAndFeel(UIManager.
                    getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // Ignore exception and keep default LookAndFeel
        }

        setupUI();
    }

    /** Initializes UI components and applies saved settings. */
    private void setupUI() {
        darkMode = Boolean.parseBoolean(ConfigDataRetriever.get("dark_mode"));
        setTitle("Diglott Translator");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputLangBox = new JComboBox<>(new String[]{"en"});
        targetLangBox = new JComboBox<>(LanguageCodes.
                LANGUAGES.keySet().toArray(new String[0]));

        inputLangBox.setSelectedItem("en-us");
        targetLangBox.setSelectedItem(
                LanguageCodes.REVERSELANGUAGES.
                        get(ConfigDataRetriever.get("target_language"))
        );

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        saveButton = new JButton("Saves");
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
        langPanel.setBorder(BorderFactory.createEmptyBorder(SMALLUIELEMENT,
                SMALLUIELEMENT, SMALLUIELEMENT, SMALLUIELEMENT));

        JPanel languageRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        languageRow.add(new JLabel("From:"));
        languageRow.add(inputLangBox);
        languageRow.add(Box.createHorizontalStrut(MEDIUMUIELEMENT));
        languageRow.add(new JLabel("To:"));
        languageRow.add(targetLangBox);

        langPanel.add(languageRow);

        JPanel buttonPanel = new JPanel(
                new GridLayout(ROWS, COLS, SMALLUIELEMENT, SMALLUIELEMENT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(
                MEDIUMUIELEMENT, MEDIUMUIELEMENT, 0, MEDIUMUIELEMENT));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(saveButton);
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
                JOptionPane.showMessageDialog(
                        this, "Book loaded successfully!");
                pickFileButton.setText("Loaded: " + result.getFile().getName());
            }
        });

        saveButton.addActionListener(e -> {
            dispose();
            new SaveUI(darkMode, translatorUseCase, speakController);
        });

        startButton.addActionListener(e -> {
            if (bookText == null) {
                JOptionPane.showMessageDialog(
                        this, "Please load a book first.");
                return;
            }

            String selectedKey = (String) targetLangBox.getSelectedItem();
            String languageCode = LanguageCodes.LANGUAGES.get(selectedKey);
            if (languageCode != null) {
                ConfigDataRetriever.set("target_language", languageCode);
                ConfigDataRetriever.saveConfig();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid language selected.",
                        "Language Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            translatorUseCase.execute(pages.get(0));

            final String credsPath =
                    ConfigDataRetriever.get("credentials_path");
            final Speaker speaker = new SpeechManager(credsPath);
            final SpeakWordsUseCase speakUseCase =
                    new SpeakWordsInteractor(speaker);
            speakController = new SpeakController(speakUseCase, speaker);

            Book book = new Book(
                    selectedFile.getName().replace(".txt", ""), pages);
            new PageUI(book, darkMode, translatorUseCase,
                    speakController).setVisible(true);

            dispose();
        });

        settingsButton.addActionListener(e ->
                new SettingsUI());

        logoutButton.addActionListener(e -> {
            ConfigDataRetriever.set("deepl_api_key", "none");
            ConfigDataRetriever.set("azure_api_key", "none");
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
