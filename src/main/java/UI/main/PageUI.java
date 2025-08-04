package UI.main;

import Configuration.ConfigDataRetriever;
import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.gateway.Speaker;
import domain.model.Page;
import infrastructure.tts.SpeechManager;
import application.interactor.SpeakWordsInteractor;
import application.usecase.SpeakWordsUseCase;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import java.io.File;
import java.util.List;

/**
 * UI window for displaying book pages and navigating between them.
 */
public class PageUI extends JFrame {

    /** Dark mode background color. */
    private static final Color DARK_BG = Color.DARK_GRAY;

    /** Dark mode foreground (text) color. */
    private static final Color DARK_FG = Color.WHITE;

    /** Index of the currently displayed page in the list. */
    private int currentPage;

    /** List of all pages in the book. */
    private final List<Page> pages;

    /** Editor pane used for displaying HTML-formatted page content. */
    private final JEditorPane content;

    /** Whether dark mode is currently enabled. */
    private final boolean darkMode;

    /** Translator use case for translating the displayed pages. */
    private final TranslatePageUseCase translator;

    /** Label for showing the current page number and total page count. */
    private final JLabel pageLabel;

    /**
     * Creates a PageUI instance for viewing and navigating pages.
     *
     * @param pageSet    the list of pages to display
     * @param darkMode   whether dark mode is enabled
     * @param translator the translator use case for translating pages
     */
    public PageUI(final List<Page> pageSet, final boolean darkMode,
                  final TranslatePageUseCase translator) {
        this.pages = pageSet;
        this.darkMode = darkMode;
        this.translator = translator;
        this.speakController = speakController;
        this.currentPage = 0;

        setTitle("Page View");
        setSize(600, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);
        setupStyle();

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.getViewport().setBackground(darkMode ? DARK_BG : Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JButton nextPage = new JButton("Next Page");
        JButton previousPage = new JButton("Last Page");
        JButton backButton = new JButton("Back to Main Page");
        pageLabel = new JLabel("", SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(previousPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(pageLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        nextPage.setEnabled(pages.size() > 1);
        previousPage.setEnabled(false);

        backButton.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("api_key"));
        });

        nextPage.addActionListener(e -> goToNextPage(previousPage, nextPage));
        previousPage.addActionListener(e -> goToPreviousPage(previousPage, nextPage));

        speakButton.addActionListener(e -> {
            String credsPath = ConfigDataRetriever.get("google_credentials_path");
            if (credsPath == null || credsPath.equals("none")) {
                JOptionPane.showMessageDialog(this, "Google credentials not found. Please re-login.");
                return;
            }

            try {
                Speaker speaker = new SpeechManager(credsPath);
                SpeakWordsUseCase speakUseCase = new SpeakWordsInteractor(speaker);
                SpeakController tempController = new SpeakController(speakUseCase);
                new SpeakUI(pages.get(currentPage), tempController, darkMode);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to initialize Text-to-Speech: " + ex.getMessage(), "TTS Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    goToNextPage(previousPage, nextPage);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    goToPreviousPage(previousPage, nextPage);
                }
            }
        });

        if (darkMode) {
            applyDarkTheme(buttonPanel, backButton, nextPage, previousPage);
        }

        updateContent();
    }

    /** Sets up HTML/CSS styling for the page content. */
    private void setupStyle() {
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule(
                "body { font-family: " + ConfigDataRetriever.get("font") + "; font-size: "
                        + ConfigDataRetriever.get("font_size") + "; color: "
                        + (darkMode ? "white" : "black") + "; background-color: "
                        + (darkMode ? "#333333" : "white") + "; }"
        );
        kit.setStyleSheet(styleSheet);
        content.setEditorKit(kit);
    }

    /** Updates the displayed page content and the page label. */
    private void updateContent() {
        String htmlContent = "<html><body>" + pages.get(currentPage).getContent() + "</body></html>";
        content.setText(htmlContent);
        content.setCaretPosition(0);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + pages.size());
    }

    /**
     * Moves to the next page if available and triggers translation if needed.
     *
     * @param previousPage the previous page button
     * @param nextPage     the next page button
     */
    private void goToNextPage(final JButton previousPage, final JButton nextPage) {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            if (!pages.get(currentPage).isTranslated()) {
                translator.execute(pages.get(currentPage));
            }
            updateContent();
            previousPage.setEnabled(true);
        }
        if (currentPage == pages.size() - 1) {
            nextPage.setEnabled(false);
        }
    }

    /**
     * Moves to the previous page if available.
     *
     * @param previousPage the previous page button
     * @param nextPage     the next page button
     */
    private void goToPreviousPage(final JButton previousPage, final JButton nextPage) {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
            nextPage.setEnabled(true);
        }
        if (currentPage == 0) {
            previousPage.setEnabled(false);
        }
    }

    /**
     * Applies dark mode styling to the panel and given buttons.
     *
     * @param panel   the panel containing the buttons
     * @param buttons the buttons to style
     */
    private void applyDarkTheme(final JPanel panel, final JButton... buttons) {
        panel.setBackground(DARK_BG);
        pageLabel.setForeground(DARK_FG);
        pageLabel.setBackground(DARK_BG);
        pageLabel.setOpaque(true);

        for (JButton button : buttons) {
            button.setBackground(DARK_BG);
            button.setForeground(DARK_FG);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }
    }
}
