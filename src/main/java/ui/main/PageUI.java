package ui.main;

import configuration.ConfigDataRetriever;
import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.model.Book;
import domain.model.Page;
import infrastructure.exporter.SaveBook;
import infrastructure.translation.PageTranslationTask;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UI window for displaying book pages and navigating between them,
 * refactored to use Book model directly.
 */
public class PageUI extends JFrame {

    /** Background color in dark mode. */
    private static final Color DARK_BG = Color.DARK_GRAY;

    /** Foreground (text) color in dark mode. */
    private static final Color DARK_FG = Color.WHITE;

    /** Width of the UI window. */
    private static final int WIDTH = 600;

    /** Height of the UI window. */
    private static final int HEIGHT = 750;

    /** Button spacing. */
    private static final int BUTTONSIZE = 10;

    /** The book being displayed. */
    private final Book displayedBook;

    /** Whether dark mode is active. */
    private final boolean isDarkMode;

    /** Translator use case for translating content. */
    private final TranslatePageUseCase pageTranslator;

    /** Text-to-speech controller. */
    private final SpeakController speechController;

    /** Editor pane displaying the page content. */
    private final JEditorPane contentArea;

    /** Label showing current page number. */
    private final JLabel pageIndicator;

    /**
     * Constructs the PageUI window.
     *
     * @param book the book to display
     * @param darkMode whether dark mode is enabled
     * @param translatorUseCase use case for translating pages
     * @param speakCtrl controller for speaking pages
     */
    public PageUI(final Book book,
                  final boolean darkMode,
                  final TranslatePageUseCase translatorUseCase,
                  final SpeakController speakCtrl) {

        this.displayedBook = book;
        this.isDarkMode = darkMode;
        this.pageTranslator = translatorUseCase;
        this.speechController = speakCtrl;

        setTitle("Reading: " + book.getTitle());
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contentArea = new JEditorPane();
        contentArea.setContentType("text/html");
        contentArea.setEditable(false);
        configureHtmlStyle();

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.getViewport().setBackground(
                isDarkMode ? DARK_BG : Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Main Page");
        JButton prevBtn = new JButton("Last Page");
        JButton nextBtn = new JButton("Next Page");
        JButton speakBtn = new JButton("Speak");
        JButton saveButton = new JButton("Save");

        pageIndicator = new JLabel("", SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, BUTTONSIZE, BUTTONSIZE));
        buttonPanel.add(backBtn);
        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);
        buttonPanel.add(saveButton);
        buttonPanel.add(pageIndicator);
        buttonPanel.add(speakBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        nextBtn.setEnabled(book.getTotalPages() > 1);
        prevBtn.setEnabled(false);

        backBtn.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("deepl_api_key"),
                    ConfigDataRetriever.get("azure_api_key"),
                    ConfigDataRetriever.get("azure_region"));
        });

        nextBtn.addActionListener(e -> {
            book.nextPage();
            refreshContent();
            prevBtn.setEnabled(true);
            nextBtn.setEnabled(book.getCurrentPageNumber()
                    < book.getTotalPages());
        });

        prevBtn.addActionListener(e -> {
            book.previousPage();
            refreshContent();
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(book.getCurrentPageNumber() > 1);
        });

        saveButton.addActionListener(e -> {
            SaveBook saveBook = new SaveBook();
            if (saveBook.save(displayedBook)) {
                saveButton.setEnabled(false);
                saveButton.setText("Saved.");
            }
        });

        speakBtn.addActionListener(e -> {
            if (!speakCtrl.isTtsAvailable()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Google Cloud credentials not configured.",
                        "Text-to-Speech Unavailable",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                new SpeakUI(book.getCurrentPage(),
                        speechController, isDarkMode);
            }
        });

        contentArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    nextBtn.doClick();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    prevBtn.doClick();
                }
            }
        });

        if (isDarkMode) {
            applyDarkTheme(buttonPanel, backBtn,
                    prevBtn, nextBtn, speakBtn);
        }

        refreshContent();
    }

    /** Applies HTML styling to the content pane. */
    private void configureHtmlStyle() {
        HTMLEditorKit editorKit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule("body { font-family: "
                + ConfigDataRetriever.get("font")
                + "; font-size: "
                + ConfigDataRetriever.get("font_size")
                + "; color: "
                + (isDarkMode ? "white" : "black")
                + "; background-color: "
                + (isDarkMode ? "#333333" : "white") + "; }");
        editorKit.setStyleSheet(styleSheet);
        contentArea.setEditorKit(editorKit);
    }

    /**
     * Updates displayed content and triggers translation if needed.
     */
    private void refreshContent() {
        Page page = displayedBook.getCurrentPage();
        if (!page.isTranslated()) {
            translatePageInBackground(page);
        }
        contentArea.setText("<html><body>"
                + page.getContent() + "</body></html>");
        contentArea.setCaretPosition(0);
        pageIndicator.setText("Page "
                + displayedBook.getCurrentPageNumber() + " of "
                + displayedBook.getTotalPages());
    }

    /**
     * Asynchronously translates a page and updates UI if needed.
     *
     * @param page the page to translate
     */
    private void translatePageInBackground(final Page page) {
        PageTranslationTask task = new PageTranslationTask(
                pageTranslator, page, () -> {
            if (displayedBook.getCurrentPage().equals(page)) {
                refreshContent();
            }
        });
        task.execute();
    }

    /**
     * Applies dark mode styling to the UI components.
     *
     * @param panel parent panel
     * @param buttons buttons to style
     */
    private void applyDarkTheme(final JPanel panel,
                                final JButton... buttons) {
        panel.setBackground(DARK_BG);
        pageIndicator.setForeground(DARK_FG);
        pageIndicator.setBackground(DARK_BG);
        pageIndicator.setOpaque(true);

        for (JButton button : buttons) {
            button.setBackground(DARK_BG);
            button.setForeground(DARK_FG);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }
    }
}
